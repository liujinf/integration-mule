/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.test.metadata.extension.resolver;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

import java.util.Set;

public class TestMetadataResolverRuntimeExceptionFailure
    implements TypeKeysResolver, InputTypeResolver<String>, OutputTypeResolver<String> {

  @Override
  public String getCategoryName() {
    return "MetadataExtensionResolver";
  }

  @Override
  public String getResolverName() {
    return "TestMetadataResolverRuntimeExceptionFailure";
  }

  public Set<MetadataKey> getKeys(MetadataContext context) throws MetadataResolvingException {
    throw new RuntimeException("Runtime Exception thrown");
  }

  public MetadataType getInputMetadata(MetadataContext context, String key) throws MetadataResolvingException {
    throw new RuntimeException("Runtime Exception thrown");
  }

  public MetadataType getOutputType(MetadataContext context, String key) throws MetadataResolvingException {
    throw new RuntimeException("Runtime Exception thrown");
  }
}
