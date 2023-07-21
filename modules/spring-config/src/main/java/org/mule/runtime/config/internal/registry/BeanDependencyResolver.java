/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.config.internal.registry;

import org.mule.runtime.config.internal.resolvers.ConfigurationDependencyResolver;

import java.util.List;

/**
 * Bean dependency resolver interface.
 * <p/>
 * Implementation of this interface must resolve the dependencies between beans in the spring context.
 * 
 * @since 4.0
 */
public interface BeanDependencyResolver {

  /**
   * @param beanName the name of the bean to resolve dependencies
   * @return a order collection of bean objects.
   */
  List<Object> resolveBeanDependencies(String beanName);

  /**
   * @return a resolver that provides configuration dependencies
   *
   * @since 4.5.0
   */
  ConfigurationDependencyResolver getConfigurationDependencyResolver();

}
