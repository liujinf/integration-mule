/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.extension.api.loader.java.type;

import org.mule.api.annotation.NoImplement;

import java.util.List;

/**
 * A generic contract for any kind of component from which, a list of operations can be derived
 *
 * @since 4.0
 */
@NoImplement
interface WithOperations {

  /**
   * @return a list of {@link MethodElement}
   */
  List<OperationElement> getOperations();
}
