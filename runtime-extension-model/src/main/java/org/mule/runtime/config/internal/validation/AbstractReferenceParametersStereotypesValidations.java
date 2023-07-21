/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.config.internal.validation;

import static org.mule.runtime.ast.api.validation.ValidationResultItem.create;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import org.mule.runtime.ast.api.ArtifactAst;
import org.mule.runtime.ast.api.validation.ArtifactValidation;
import org.mule.runtime.ast.api.validation.ValidationResultItem;
import org.mule.runtime.ast.graph.api.ArtifactAstDependencyGraph;
import org.mule.runtime.ast.graph.api.ArtifactAstDependencyGraphFactory;
import org.mule.runtime.ast.graph.api.ComponentAstDependency;

import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractReferenceParametersStereotypesValidations implements ArtifactValidation {

  @Override
  public List<ValidationResultItem> validateMany(ArtifactAst artifact) {
    ArtifactAstDependencyGraph dependencyGraph = ArtifactAstDependencyGraphFactory.generateFor(artifact);
    return dependencyGraph.getMissingDependencies()
        .stream()
        .filter(filterArtifact(artifact))
        .filter(missing -> filterComponent(missing))
        .map(missing -> create(missing.getComponent(), missing.getParameter(), this,
                               validationMessage(missing)))
        .collect(toList());
  }

  /**
   * Determine whether this dependency must be analyzed or not.
   * 
   * @param missingDependency the dependency to analyze
   * @return {@code true} if this dependency must be analyzed, {@code false} if not.
   */
  protected boolean filterComponent(ComponentAstDependency missingDependency) {
    return true;
  }

  protected abstract Predicate<? super ComponentAstDependency> filterArtifact(ArtifactAst artifact);

  protected String validationMessage(ComponentAstDependency missing) {
    return format("Referenced component '%s' must be one of stereotypes %s.",
                  missing.getName(),
                  missing.getAllowedStereotypes().stream()
                      .map(st -> st.getNamespace() + ":" + st.getType())
                      .collect(joining(", ", "[", "]")));
  }

}
