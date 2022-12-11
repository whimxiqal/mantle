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

class ParameterImpl implements Parameter {

  private final String name;
  private final ParameterOptions options;
  private final Predicate<String> validator;
  private final Component invalidMessage;

  ParameterImpl(String name, ParameterOptions options,
                Predicate<String> validator, Component invalidMessage) {
    this.name = name;
    this.options = options;
    this.validator = validator;
    this.invalidMessage = invalidMessage;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public ParameterOptions options() {
    return options;
  }

  @Override
  public boolean isValid(String candidate) {
    if (validator == null) {
      return true;
    }
    return validator.test(candidate);
  }

  @Override
  public Component invalidMessage() {
    return invalidMessage;
  }
}
