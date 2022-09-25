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

package me.pietelite.mantle.common;

import java.util.UUID;
import net.kyori.adventure.audience.Audience;

public class CommandSource {

  public static CommandSource unknown() {
    return new CommandSource(Type.UNKNOWN, null, Audience.empty());
  }

  public enum Type {
    CONSOLE,
    PLAYER,
    UNKNOWN
  }

  private final Type type;
  private final UUID uuid;
  private final Audience audience;

  public CommandSource(Type type, UUID uuid, Audience audience) {
    this.type = type;
    this.uuid = uuid;
    this.audience = audience;
  }

  public Type getType() {
    return type;
  }

  public UUID getUuid() {
    return uuid;
  }

  public Audience getAudience() {
    return audience;
  }
}
