/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.module.artifact.api.descriptor;

import org.mule.runtime.api.deployment.meta.MuleArtifactLoaderDescriptor;
import org.mule.runtime.api.deployment.meta.MulePluginModel;

/**
 * Constants to be consumed across modules to generate and consume a proper
 * {@link org.mule.runtime.api.deployment.meta.AbstractMuleArtifactModel} when working with the
 * {@link MulePluginModel#getExtensionModelLoaderDescriptor()}.
 *
 * @since 4.5.0
 */
public class ArtifactDescriptorConstants {

  /**
   * Default descriptor loader ID for Mule artifacts
   */
  public static final String MULE_LOADER_ID = "mule";

  /**
   * Property to fill the {@link MuleArtifactLoaderDescriptor#getAttributes()} which defines the exported packages of a given
   * artifact.
   */
  public static final String EXPORTED_PACKAGES = "exportedPackages";

  public static final String PRIVILEGED_EXPORTED_PACKAGES = "privilegedExportedPackages";

  public static final String PRIVILEGED_ARTIFACTS_IDS = "privilegedArtifactIds";

  /**
   * Property to fill the {@link MuleArtifactLoaderDescriptor#getAttributes()} which defines the exported resources of a given
   * artifact.
   */
  public static final String EXPORTED_RESOURCES = "exportedResources";

  /**
   * Property that defines to include or not scope test dependencies when building the class loader configuration of a given
   * artifact.
   */
  public static final String INCLUDE_TEST_DEPENDENCIES = "includeTestDependencies";

  /**
   * Location of the serialized artifact AST within the deployable artifact.
   * <p>
   * This is generated by the {@code mule-maven-plugin} in versions 3.6+
   * 
   * @since 4.5
   */
  public static final String SERIALIZED_ARTIFACT_AST_LOCATION = "META-INF/mule-artifact/artifact.ast";

  protected ArtifactDescriptorConstants() {

  }
}
