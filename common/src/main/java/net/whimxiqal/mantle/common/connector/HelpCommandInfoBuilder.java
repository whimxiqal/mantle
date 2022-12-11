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

package net.whimxiqal.mantle.common.connector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.Builder;

/**
 * A builder for a {@link HelpCommandInfo}.
 */
public class HelpCommandInfoBuilder implements Builder<HelpCommandInfo> {

  private static final Component DEFAULT_HEADER = Component.text("Command Information...");
  private final Map<Integer, Component> descriptions = new HashMap<>();
  private final Set<Integer> ignored = new HashSet<>();
  private Component header;

  @Override
  public HelpCommandInfo build() {
    if (header == null) {
      header = DEFAULT_HEADER;
    }
    return new HelpCommandInfoImpl(header,
        descriptions,
        ignored);
  }

  /**
   * Set the header of all command info messages.
   *
   * @param header the header
   * @return the builder, for chaining
   */
  public HelpCommandInfoBuilder setHeader(Component header) {
    this.header = header;
    return this;
  }

  /**
   * Add a description for a rule.
   *
   * @param rule        the rule
   * @param description the description
   * @return the builder, for chaining
   */
  public HelpCommandInfoBuilder addDescription(int rule, Component description) {
    if (descriptions.putIfAbsent(rule, description) != null) {
      throw new IllegalArgumentException("A description for rule " + rule + " has already been added.");
    }
    return this;
  }

  /**
   * Add ignored rules.
   * These rules should not be considered when looking up descriptions,
   * and the description and information about a higher-level command should be given instead.
   *
   * @param rule the rule
   * @return the builder, for chaining
   */
  public HelpCommandInfoBuilder addIgnored(int rule) {
    if (!ignored.add(rule)) {
      throw new IllegalArgumentException("The rule " + rule + " is already ignored.");
    }
    return this;
  }
}
