/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.core.privileged.transport;

/**
 * @deprecated Transport infrastructure is deprecated.
 */
@Deprecated
public interface LegacyInboundEndpoint {

  boolean isCompatibleWithAsync();

  String getCanonicalURI();

}
