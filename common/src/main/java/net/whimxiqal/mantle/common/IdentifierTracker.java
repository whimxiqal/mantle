/*
 * MIT License
 *
 * Copyright (c) Pieter Svenson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.whimxiqal.mantle.common;

import java.util.List;

/**
 * A container to provide identifiers during command execution.
 */
public interface IdentifierTracker {

  /**
   * Get all identifiers.
   *
   * @return the identifiers
   */
  List<String> getAll();

  /**
   * Get all identifiers for a given parameter.
   *
   * @param parameter the parameter
   * @return the identifiers
   */
  List<String> getAll(String parameter);

  /**
   * Get an identifier at a specific index.
   *
   * @param index the index
   * @return the identifier
   * @throws IndexOutOfBoundsException if out of bounds
   */
  String get(int index) throws IndexOutOfBoundsException;

  /**
   * Get an identifier at a specific index for a specific parameter.
   *
   * @param parameter the parameter
   * @param index     the index
   * @return the identifier
   * @throws IndexOutOfBoundsException if out of bounds
   */
  String get(String parameter, int index) throws IndexOutOfBoundsException;

}
