/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.core.internal.profiling;

import static org.mule.runtime.api.config.MuleRuntimeFeature.ENABLE_PROFILING_SERVICE;
import static org.mule.runtime.tracer.exporter.config.api.OpenTelemetrySpanExporterConfigurationProperties.MULE_OPEN_TELEMETRY_EXPORTER_ENABLED;

import static java.lang.Boolean.parseBoolean;

import org.mule.runtime.api.config.FeatureFlaggingService;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.lifecycle.Lifecycle;
import org.mule.runtime.api.lifecycle.Startable;
import org.mule.runtime.api.lifecycle.Stoppable;
import org.mule.runtime.api.profiling.ProfilingDataConsumer;
import org.mule.runtime.api.profiling.ProfilingDataProducer;
import org.mule.runtime.api.profiling.ProfilingEventContext;
import org.mule.runtime.api.profiling.ProfilingProducerScope;
import org.mule.runtime.api.profiling.threading.ThreadSnapshotCollector;
import org.mule.runtime.api.profiling.tracing.ExecutionContext;
import org.mule.runtime.api.profiling.tracing.TracingService;
import org.mule.runtime.api.profiling.type.ProfilingEventType;
import org.mule.runtime.ast.api.exception.PropertyNotFoundException;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.event.CoreEvent;
import org.mule.runtime.tracer.api.sniffer.SpanSnifferManager;
import org.mule.runtime.tracer.api.EventTracer;
import org.mule.runtime.tracer.api.context.getter.DistributedTraceContextGetter;
import org.mule.runtime.tracer.exporter.config.api.SpanExporterConfiguration;
import org.mule.runtime.core.privileged.profiling.PrivilegedProfilingService;

import java.util.function.Function;

import javax.inject.Inject;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A {@link InternalProfilingService} that may not produce profiling data if the profiling functionality is totally disabled.
 *
 * @see DefaultProfilingService
 * @see NoOpProfilingService
 */
public class ProfilingServiceWrapper implements InternalProfilingService, PrivilegedProfilingService, Lifecycle {

  @Inject
  protected MuleContext muleContext;

  @Inject
  protected FeatureFlaggingService featureFlaggingService;

  @Inject
  protected EventTracer<CoreEvent> coreEventTracer;

  @Inject
  protected SpanExporterConfiguration spanExporterConfiguration;

  private InternalProfilingService profilingService;
  private EventTracer<CoreEvent> updatableCoreEvenTracer;

  @Override
  public <T extends ProfilingEventContext, S> ProfilingDataProducer<T, S> getProfilingDataProducer(
                                                                                                   ProfilingEventType<T> profilingEventType) {
    return getProfilingService().getProfilingDataProducer(profilingEventType);
  }

  @Override
  public <T extends ProfilingEventContext, S> ProfilingDataProducer<T, S> getProfilingDataProducer(
                                                                                                   ProfilingEventType<T> profilingEventType,
                                                                                                   ProfilingProducerScope producerScope) {
    return getProfilingService().getProfilingDataProducer(profilingEventType, producerScope);
  }

  @Override
  public <T extends ProfilingEventContext, S> void registerProfilingDataProducer(ProfilingEventType<T> profilingEventType,
                                                                                 ProfilingDataProducer<T, S> profilingDataProducer) {
    getProfilingService().registerProfilingDataProducer(profilingEventType, profilingDataProducer);
  }

  @Override
  public <T extends ProfilingEventContext> void registerProfilingDataConsumer(ProfilingDataConsumer<T> profilingDataConsumer) {
    // This is a privileged operation that has only to be invoked by a certain test connectors
    // The API for this will not be generally available.
    if (profilingService instanceof PrivilegedProfilingService) {
      ((PrivilegedProfilingService) getProfilingService()).registerProfilingDataConsumer(profilingDataConsumer);
    }
  }

  @Override
  public void injectDistributedTraceContext(EventContext eventContext,
                                            DistributedTraceContextGetter distributedTraceContextGetter) {
    if (profilingService instanceof PrivilegedProfilingService) {
      ((PrivilegedProfilingService) getProfilingService()).injectDistributedTraceContext(eventContext,
                                                                                         distributedTraceContextGetter);
    }
  }

  @Override
  public ThreadSnapshotCollector getThreadSnapshotCollector() {
    return getProfilingService().getThreadSnapshotCollector();
  }

  @Override
  public TracingService getTracingService() {
    return getProfilingService().getTracingService();
  }

  public InternalProfilingService getProfilingService() throws MuleRuntimeException {
    if (profilingService != null) {
      return profilingService;
    }
    return initialiseProfilingService();
  }

  @Override
  public SpanSnifferManager getSpanExportManager() {
    if (profilingService instanceof PrivilegedProfilingService) {
      return ((PrivilegedProfilingService) getProfilingService()).getSpanExportManager();
    }

    return PrivilegedProfilingService.super.getSpanExportManager();
  }

  private InternalProfilingService initialiseProfilingService() throws MuleRuntimeException {
    if (featureFlaggingService.isEnabled(ENABLE_PROFILING_SERVICE)) {
      profilingService = new DefaultProfilingService();
    } else {
      profilingService = new NoOpProfilingService();
    }

    try {
      muleContext.getInjector().inject(profilingService);
    } catch (MuleException e) {
      throw new MuleRuntimeException(e);
    }

    return profilingService;
  }

  @Override
  public <T extends ProfilingEventContext, S> Mono<S> enrichWithProfilingEventMono(Mono<S> original,
                                                                                   ProfilingDataProducer<T, S> dataProducer,
                                                                                   Function<S, T> transformer) {
    return profilingService.enrichWithProfilingEventMono(original, dataProducer, transformer);
  }

  @Override
  public <T extends ProfilingEventContext, S> Flux<S> enrichWithProfilingEventFlux(Flux<S> original,
                                                                                   ProfilingDataProducer<T, S> dataProducer,
                                                                                   Function<S, T> transformer) {
    return profilingService.enrichWithProfilingEventFlux(original, dataProducer, transformer);
  }

  @Override
  public <S> Mono<S> setCurrentExecutionContext(Mono<S> original, Function<S, ExecutionContext> executionContextSupplier) {
    return profilingService.setCurrentExecutionContext(original, executionContextSupplier);
  }

  @Override
  public <S> Flux<S> setCurrentExecutionContext(Flux<S> original, Function<S, ExecutionContext> executionContextSupplier) {
    return profilingService.setCurrentExecutionContext(original, executionContextSupplier);
  }

  @Override
  public EventTracer<CoreEvent> getCoreEventTracer() {
    if (updatableCoreEvenTracer == null) {
      updatableCoreEvenTracer = resolveUpdatableCoreEventTracer();
    }
    return updatableCoreEvenTracer;
  }

  private EventTracer<CoreEvent> resolveUpdatableCoreEventTracer() {
    final SelectableCoreEventTracer selectableCoreEventTracer =
        new SelectableCoreEventTracer(coreEventTracer, getProfilingService().getCoreEventTracer(), isTracingExportEnabled());
    spanExporterConfiguration
        .doOnConfigurationChanged(() -> selectableCoreEventTracer.useFirstCoreEventTracer(isTracingExportEnabled()));
    return selectableCoreEventTracer;
  }

  private boolean isTracingExportEnabled() {
    try {
      return parseBoolean(spanExporterConfiguration.getStringValue(MULE_OPEN_TELEMETRY_EXPORTER_ENABLED, "false"));
    } catch (PropertyNotFoundException e) {
      return false;
    }
  }

  @Override
  public void dispose() {
    if (profilingService == null) {
      initialiseProfilingService();
    }

    if (profilingService instanceof Disposable) {
      ((Disposable) profilingService).dispose();
    }
  }

  @Override
  public void initialise() throws InitialisationException {
    if (profilingService == null) {
      initialiseProfilingService();
    }

    if (profilingService instanceof Initialisable) {
      ((Initialisable) profilingService).initialise();
    }
  }

  @Override
  public void start() throws MuleException {
    if (profilingService == null) {
      initialiseProfilingService();
    }

    if (profilingService instanceof Startable) {
      ((Startable) profilingService).start();
    }
  }

  @Override
  public void stop() throws MuleException {
    if (profilingService == null) {
      initialiseProfilingService();
    }

    if (profilingService instanceof Stoppable) {
      ((Stoppable) profilingService).stop();
    }
  }
}
