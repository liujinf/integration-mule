/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.module.artifact.activation.internal.plugin;

import org.mule.tools.api.classloader.model.ArtifactCoordinates;

import java.net.URL;
import java.util.List;

/**
 * Resolves the patches that must be applied to a given plugin.
 *
 * @since 4.5
 */
public interface PluginPatchesResolver {

  /**
   * @param pluginArtifactCoordinates artifact coordinates of the plugin to resolve patches for.
   * @return a {@link List} of {@link URL}s indicating the location of the patches that apply to the plugin.
   */
  List<URL> resolve(ArtifactCoordinates pluginArtifactCoordinates);
}
