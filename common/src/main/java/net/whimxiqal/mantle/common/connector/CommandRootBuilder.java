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

import java.util.LinkedList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.Builder;

/**
 * A builder for a {@link CommandRoot}.
 */
public class CommandRootBuilder implements Builder<CommandRoot> {

  private final String baseCommand;
  private final List<String> aliases = new LinkedList<>();
  private Component description;

  public CommandRootBuilder(String baseCommand) {
    this.baseCommand = baseCommand;
  }

  @Override
  public CommandRoot build() {
    return new CommandRootImpl(baseCommand, description, aliases);
  }

  /**
   * Set a description for this command.
   *
   * @param description the description
   * @return the builder, for chaining
   */
  public CommandRootBuilder description(Component description) {
    this.description = description;
    return this;
  }

  /**
   * Add an alias for this command. You may add multiple.
   *
   * @param alias the alias
   * @return the builder, for chaining
   */
  public CommandRootBuilder addAlias(String alias) {
    this.aliases.add(alias);
    return this;
  }

}
