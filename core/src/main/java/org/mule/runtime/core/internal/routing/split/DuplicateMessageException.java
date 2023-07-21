/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.core.internal.routing.split;

import org.mule.runtime.core.internal.exception.ValidationException;

/**
 * Indicates that the execution of the current event is stopped becasue the message has already been processed. This exception is
 * thrown to indicate this condition to the source of the flow.
 * 
 * @since 4.0
 */
public class DuplicateMessageException extends ValidationException {

  private static final long serialVersionUID = -356337746508371704L;

}
