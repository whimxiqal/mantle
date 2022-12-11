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

import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class CommandRootImpl implements CommandRoot {

  private final String baseCommand;
  private final Component description;
  private final List<String> aliases;

  CommandRootImpl(String baseCommand,
                  Component description,
                  List<String> aliases) {
    this.baseCommand = baseCommand;
    this.description = description;
    this.aliases = Collections.unmodifiableList(aliases);
  }

  @Override
  public String baseCommand() {
    return baseCommand;
  }

  @Override
  public Component description() {
    return description;
  }

  @Override
  public List<String> aliases() {
    return aliases;
  }

}
