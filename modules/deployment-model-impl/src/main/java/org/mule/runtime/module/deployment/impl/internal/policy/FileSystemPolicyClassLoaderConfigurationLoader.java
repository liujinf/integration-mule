/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.module.deployment.impl.internal.policy;

import org.mule.runtime.core.api.config.bootstrap.ArtifactType;
import org.mule.runtime.module.artifact.api.descriptor.ClassLoaderConfiguration;
import org.mule.runtime.module.artifact.api.descriptor.ClassLoaderConfiguration.ClassLoaderConfigurationBuilder;
import org.mule.runtime.module.artifact.api.descriptor.ClassLoaderConfigurationLoader;
import org.mule.runtime.module.artifact.api.descriptor.InvalidDescriptorLoaderException;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.io.filefilter.SuffixFileFilter;

/**
 * Creates a {@link ClassLoaderConfiguration} from a policy's folder
 */
public class FileSystemPolicyClassLoaderConfigurationLoader implements ClassLoaderConfigurationLoader {

  public static final String FILE_SYSTEM_POLICY_MODEL_LOADER_ID = "FILE_SYSTEM_POLICY_MODEL_LOADER";
  static final String LIB_DIR = "lib";
  private static final String JAR_FILE = ".jar";


  @Override
  public String getId() {
    return FILE_SYSTEM_POLICY_MODEL_LOADER_ID;
  }

  /**
   * Given a policy template's location it will build a {@link ClassLoaderConfiguration} taking in account jars located inside the
   * {@value LIB_DIR} folder and resources located inside the artifact folder.
   *
   * @param artifactFolder {@link File} where the current plugin to work with.
   * @param attributes     collection of attributes describing the loader. Non null.
   * @param artifactType   artifactType the type of the artifact of the descriptor to be loaded.
   * 
   * @return a {@link ClassLoaderConfiguration} loaded with all its dependencies and URLs
   */
  @Override
  public ClassLoaderConfiguration load(File artifactFolder, Map<String, Object> attributes, ArtifactType artifactType)
      throws InvalidDescriptorLoaderException {

    final ClassLoaderConfigurationBuilder classLoaderConfigurationBuilder = new ClassLoaderConfigurationBuilder();

    loadUrls(classLoaderConfigurationBuilder, artifactFolder);

    return classLoaderConfigurationBuilder.build();
  }

  @Override
  public boolean supportsArtifactType(ArtifactType artifactType) {
    return true;
  }

  private void loadUrls(ClassLoaderConfigurationBuilder classLoaderConfigurationBuilder, File artifactFolder)
      throws InvalidDescriptorLoaderException {
    try {
      classLoaderConfigurationBuilder.containing(artifactFolder.toURI().toURL());
      final File libDir = new File(artifactFolder, LIB_DIR);
      if (libDir.exists()) {
        final File[] jars = libDir.listFiles((FilenameFilter) new SuffixFileFilter(JAR_FILE));
        for (int i = 0; i < jars.length; i++) {
          classLoaderConfigurationBuilder.containing(jars[i].toURI().toURL());
        }
      }
    } catch (MalformedURLException e) {
      throw new InvalidDescriptorLoaderException("Failed to create plugin descriptor " + artifactFolder);
    }
  }
}
