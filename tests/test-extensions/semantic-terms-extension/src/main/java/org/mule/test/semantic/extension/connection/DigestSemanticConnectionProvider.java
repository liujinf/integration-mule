/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.test.semantic.extension.connection;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.sdk.api.annotation.semantics.connectivity.DigestAuth;

@DigestAuth
@Alias("digest")
public class DigestSemanticConnectionProvider extends SemanticTermsConnectionProvider {

}
