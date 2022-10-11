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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.pietelite.mantle.common.CommandExecutor;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

public class CommandConnectorBuilder {

  private final List<CommandRoot> roots = new LinkedList<>();
  private final Map<Integer, String> rulePermissions = new HashMap<>();
  private Class<? extends Lexer> lexerClass;
  private Class<? extends Parser> parserClass;
  private CommandExecutor executor;
  private HelpCommandInfo helpCommandInfo;
  private CompletionInfo completionInfo;
  private final Set<Integer> playerOnlyCommands = new HashSet<>();
  private boolean useDefaultParseError = true;

  public CommandConnector build() {
    if (roots.isEmpty()) {
      throw new InvalidCommandConnector("There must be at least one command root");
    }
    if (lexerClass == null) {
      throw new InvalidCommandConnector("The lexerClass of a Mantle Command Connector may not be null");
    }
    if (parserClass == null) {
      throw new InvalidCommandConnector("The parserClass of a Mantle Command Connector may not be null");
    }
    if (executor == null) {
      throw new InvalidCommandConnector("The executor of a Mantle Command Connector may not be null");
    }
    if (completionInfo == null) {
      completionInfo = CompletionInfo.builder().build();
    }
    return new CommandConnectorImpl(roots,
        lexerClass,
        parserClass,
        executor,
        helpCommandInfo,
        rulePermissions,
        completionInfo,
        playerOnlyCommands,
        useDefaultParseError);
  }

  public CommandConnectorBuilder addRoot(CommandRoot root) {
    this.roots.add(root);
    return this;
  }

  public CommandConnectorBuilder lexer(Class<? extends Lexer> lexerClass) {
    this.lexerClass = lexerClass;
    return this;
  }

  public CommandConnectorBuilder parser(Class<? extends Parser> parserClass) {
    this.parserClass = parserClass;
    return this;
  }

  public CommandConnectorBuilder executor(CommandExecutor executor) {
    this.executor = executor;
    return this;
  }

  public CommandConnectorBuilder helpInfo(HelpCommandInfo helpCommandInfo) {
    this.helpCommandInfo = helpCommandInfo;
    return this;
  }

  public CommandConnectorBuilder addPermission(int rule, String permission) {
    if (rulePermissions.putIfAbsent(rule, permission) != null) {
      throw new IllegalArgumentException("There already exists a permission for rule number " + rule);
    }
    return this;
  }

  public CommandConnectorBuilder completionInfo(CompletionInfo completionInfo) {
    this.completionInfo = completionInfo;
    return this;
  }

  public CommandConnectorBuilder playerOnlyCommands(Integer... commands) {
    this.playerOnlyCommands.addAll(Arrays.asList(commands));
    return this;
  }

  public void setUseDefaultParseError(boolean use) {
    this.useDefaultParseError = use;
  }
}
