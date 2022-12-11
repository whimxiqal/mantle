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

import java.util.Optional;
import net.kyori.adventure.text.Component;

/**
 * A result of a command.
 */
public interface CommandResult {

  /**
   * Generate a builder for a command result.
   *
   * @return the builder
   */
  static CommandResultBuilder builder() {
    return new CommandResultBuilder();
  }

  /**
   * Generate a basic success command result.
   *
   * @return a success result
   */
  static CommandResult success() {
    return builder().type(Type.SUCCESS).build();
  }

  /**
   * Generate a basic failure command result.
   *
   * @return a basic failure
   */
  static CommandResult failure() {
    return builder().type(Type.FAILURE).build();
  }

  /**
   * Generate a basic failure command result with a message.
   * <b>The message is currently unused.</b>
   *
   * @param message a basic failure with a message
   * @return a failure
   */
  static CommandResult failure(Component message) {
    return builder().type(Type.FAILURE).message(message).build();
  }

  /**
   * The type of result.
   *
   * @return the result's type
   */
  Type type();

  /**
   * An optional message associated with the result.
   * <b>Currently Unused</b>
   *
   * @return the message
   */
  Optional<Component> message();

  /**
   * The type of command result.
   */
  enum Type {
    EMPTY,
    SUCCESS,
    FAILURE
  }

}
