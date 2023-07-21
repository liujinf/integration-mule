/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.config.internal.bean.lazy;

import static org.mule.runtime.api.exception.ExceptionHelper.getRootException;
import static org.mule.runtime.api.value.ResolvingFailure.Builder.newFailure;
import static org.mule.runtime.api.value.ValueResult.resultFrom;
import static org.mule.runtime.core.internal.util.LocationUtils.deleteLastPartFromLocation;
import static org.mule.runtime.core.internal.util.LocationUtils.isConnection;
import static org.mule.runtime.extension.api.values.ValueResolvingException.INVALID_LOCATION;
import static org.mule.runtime.extension.api.values.ValueResolvingException.UNKNOWN;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import org.mule.runtime.api.component.location.ConfigurationComponentLocator;
import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.value.ValueProviderService;
import org.mule.runtime.api.value.ValueResult;
import org.mule.runtime.config.internal.context.lazy.LazyComponentInitializerAdapter;
import org.mule.runtime.config.internal.dsl.model.NoSuchComponentModelException;

import java.util.Optional;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * {@link ValueProviderService} implementation flavour that initialises just the required components before executing the
 * resolving logic.
 * <p>
 * This guarantees that if the application has been created lazily, the requested components exists before the execution of the
 * actual {@link ValueProviderService}.
 *
 * @since 4.0
 * @see ValueProviderService
 */
public class LazyValueProviderService implements ValueProviderService, Initialisable {

  public static final String NON_LAZY_VALUE_PROVIDER_SERVICE = "_muleNonLazyValueProviderService";
  private final Supplier<ValueProviderService> valueProviderServiceSupplier;
  private final Supplier<ConfigurationComponentLocator> componentLocatorSupplier;

  private final LazyComponentInitializerAdapter lazyComponentInitializer;

  @Inject
  @Named(NON_LAZY_VALUE_PROVIDER_SERVICE)
  private ValueProviderService providerService;

  private ConfigurationComponentLocator componentLocator;

  public LazyValueProviderService(LazyComponentInitializerAdapter lazyComponentInitializer,
                                  Supplier<ValueProviderService> valueProviderServiceSupplier,
                                  Supplier<ConfigurationComponentLocator> componentLocatorSupplier) {
    this.lazyComponentInitializer = lazyComponentInitializer;
    this.valueProviderServiceSupplier = valueProviderServiceSupplier;
    this.componentLocatorSupplier = componentLocatorSupplier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ValueResult getValues(Location location, String providerName) {
    return initializeComponent(location)
        .orElseGet(() -> providerService.getValues(location, providerName));
  }

  @Override
  public ValueResult getFieldValues(Location location, String parameter, String targetSelector) {
    return initializeComponent(location)
        .orElseGet(() -> providerService.getFieldValues(location, parameter, targetSelector));
  }

  private Optional<ValueResult> initializeComponent(Location location) {
    Location locationWithOutConnection = locationWithOutConnection(location);

    try {
      lazyComponentInitializer.initializeComponent(locationWithOutConnection, false);
    } catch (Exception e) {
      Throwable rootException = getRootException(e);
      if (rootException instanceof NoSuchComponentModelException) {
        return of(resultFrom(newFailure(e)
            .withFailureCode(INVALID_LOCATION)
            .withMessage(format("Unable to resolve values. No component was found in the given location [%s]", location))
            .build()));
      }

      return of(resultFrom(newFailure(e)
          .withMessage("Unknown error occurred trying to resolve values. " + e.getMessage())
          .withFailureCode(UNKNOWN)
          .build()));
    }

    return empty();
  }

  private Location locationWithOutConnection(Location location) {
    return isConnection(location) ? deleteLastPartFromLocation(location) : location;
  }

  @Override
  public void initialise() throws InitialisationException {
    this.providerService = valueProviderServiceSupplier.get();
    this.componentLocator = componentLocatorSupplier.get();
  }
}
