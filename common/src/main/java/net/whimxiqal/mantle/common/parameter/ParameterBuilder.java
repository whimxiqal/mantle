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

package net.whimxiqal.mantle.common.parameter;

import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.whimxiqal.mantle.common.Builder;

/**
 * A builder for a {@link Parameter}.
 */
public class ParameterBuilder implements Builder<Parameter> {

  private final String name;
  private ParameterOptions options;
  private Predicate<String> validator;
  private Component invalidMessage;

  public ParameterBuilder(String name) {
    this.name = name;
  }

  /**
   * Set the logic for determining the list of possible options a user has for this parameter.
   *
   * @param options the options
   * @return builder, for chaining
   */
  public ParameterBuilder options(ParameterOptions options) {
    this.options = options;
    return this;
  }

  /**
   * A validator for a given input string for this parameter.
   *
   * @param validator the validator
   * @return builder, for chaining
   */
  public ParameterBuilder validator(Predicate<String> validator) {
    this.validator = validator;
    return this;
  }

  /**
   * Set the message to send to the user if the validator fails.
   *
   * @param component the message
   * @return builder, for chaining
   */
  public ParameterBuilder invalidMessage(Component component) {
    this.invalidMessage = component;
    return this;
  }

  @Override
  public Parameter build() {
    if (invalidMessage == null) {
      invalidMessage = Component.text("That input is invalid").color(NamedTextColor.DARK_RED);
    }
    return new ParameterImpl(name, options, validator, invalidMessage);
  }
}
