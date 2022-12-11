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

package net.whimxiqal.mantle.common.phase;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.whimxiqal.mantle.common.CommandResult;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class PermissionParsePhase implements ParsePhase {

  private final CommandConnector connector;

  public PermissionParsePhase(CommandConnector connector) {
    this.connector = connector;
  }

  @Override
  public Optional<CommandResult> walk(CommandSource source, ParseTree parseTree) {
    Map<Integer, String> rulePermissions = connector.rulePermissions();
    if (rulePermissions == null) {
      rulePermissions = Collections.emptyMap();
    }
    PermissionListener permissionListener = new PermissionListener(source, rulePermissions);
    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(permissionListener, parseTree);
    if (!permissionListener.isAllowed()) {
      source.audience().sendMessage(Component.text("You do not have permission to do that").color(NamedTextColor.DARK_RED));
      return Optional.of(CommandResult.failure());
    }
    return Optional.empty();
  }
}
