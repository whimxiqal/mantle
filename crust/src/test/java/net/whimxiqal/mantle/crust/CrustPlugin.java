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

package net.whimxiqal.mantle.crust;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.whimxiqal.mantle.common.CommandResult;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.MantleCommand;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;

public class CrustPlugin {

  public static CrustPlugin instance;
  private final Map<String, MantleCommand> commands = new HashMap<>();
  public final Map<UUID, Set<String>> playerRestrictedPermissions = new HashMap<>();

  public void registerCommand(CommandConnector connector) {
    for (CommandRoot root : connector.roots()) {
      commands.put(root.baseCommand(), new MantleCommand(connector, root));
    }
  }

  public CommandResult executeCommand(CommandSource source, String command) {
    String[] tokens = command.split(" ", 2);
    return commands.get(tokens[0]).process(source, tokens.length < 2 ? "" : tokens[1]);
  }

  public List<String> completeCommand(CommandSource source, String command) {
    String[] tokens = command.split(" ", 2);
    return commands.get(tokens[0]).complete(source, tokens.length < 2 ? "" : tokens[1]);
  }

  public void revokePermission(UUID playerUuid, String permission) {
    this.playerRestrictedPermissions.computeIfAbsent(playerUuid, k -> new HashSet<>()).add(permission);
  }

}
