/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.config.internal.factories;

import org.mule.runtime.api.time.TimeSupplier;
import org.mule.runtime.dsl.api.component.AbstractComponentFactory;
import org.mule.runtime.dsl.api.component.ObjectFactory;
import org.mule.runtime.extension.api.runtime.ExpirationPolicy;
import org.mule.runtime.core.internal.config.ImmutableExpirationPolicy;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * An {@link ObjectFactory} which produces instances of {@link ExpirationPolicy}
 *
 * @since 4.0
 */
public class ExpirationPolicyObjectFactory extends AbstractComponentFactory<ExpirationPolicy> {

  @Inject
  private TimeSupplier timeSupplier;

  private Long maxIdleTime = null;
  private TimeUnit timeUnit = null;

  public void setMaxIdleTime(Long maxIdleTime) {
    this.maxIdleTime = maxIdleTime;
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  @Override
  public ExpirationPolicy doGetObject() throws Exception {
    if (maxIdleTime != null && timeUnit != null) {
      return new ImmutableExpirationPolicy(maxIdleTime, timeUnit, timeSupplier);
    }
    return ImmutableExpirationPolicy.getDefault(timeSupplier);
  }
}
