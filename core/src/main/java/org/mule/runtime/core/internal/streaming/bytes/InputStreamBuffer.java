/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.core.internal.streaming.bytes;

import java.nio.ByteBuffer;

/**
 * A buffer which provides concurrent random access to the entirety of a dataset.
 * <p>
 * It works with the concept of a zero-base position. Each position represents one byte in the stream. Although this buffer tracks
 * the position of each byte, it doesn't have a position itself. That means that pulling data from this buffer does not make any
 * current position to be moved.
 *
 * @since 4.0
 */
public interface InputStreamBuffer {

  /**
   * Returns a ByteBuffer with up to {@code length} amount of bytes starting from the given {@code position}. The returned buffer
   * may contain less information than requested.
   *
   * If no information is available at all, then it returns {@code null}
   *
   * @param position the stream position from which the data should be read
   * @param length   how many bytes to read
   * @return A {@link ByteBuffer} with up to {@code length} bytes of information or {@code null} if no information available at
   *         all
   */
  ByteBuffer get(long position, int length);

  /**
   * Releases all the resources held by this buffer
   */
  void close();
}
