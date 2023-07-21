/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.test.metadata.extension.model.response;

import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.test.metadata.extension.resolver.TestMetadataInputHouseResolver;

import java.io.InputStream;

public class ErrorResponse {

  @Parameter
  @TypeResolver(TestMetadataInputHouseResolver.class)
  private InputStream response;

  @Parameter
  private Integer code;

  public InputStream getResponse() {
    return response;
  }

  public Integer getCode() {
    return code;
  }
}
