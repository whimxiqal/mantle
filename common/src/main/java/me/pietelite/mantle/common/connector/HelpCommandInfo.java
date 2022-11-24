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

package me.pietelite.mantle.common.connector;

import java.util.Map;
import java.util.Set;
import net.kyori.adventure.text.Component;

/**
 * General information about a help command.
 * A help command sends information to the user about how to use a command.
 */
public interface HelpCommandInfo {

  static HelpCommandInfoBuilder builder() {
    return new HelpCommandInfoBuilder();
  }

  /**
   * The header of the help command.
   *
   * @return the header
   */
  Component header();

  /**
   * All descriptions, keyed underneath the indexes of their corresponding parser rules.
   *
   * @return the descriptions
   */
  Map<Integer, Component> descriptions();

  /**
   * Get all ignored rules. These rules should not be considered when looking up descriptions,
   * and the description and information about a higher-level command should be given instead.
   *
   * @return ignored rules
   */
  Set<Integer> ignored();

}
