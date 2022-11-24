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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import me.pietelite.mantle.common.CommandExecutor;
import me.pietelite.mantle.common.Mantle;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.jetbrains.annotations.Nullable;

class CommandConnectorImpl implements CommandConnector {

  private final Collection<CommandRoot> roots;
  private final Class<? extends Lexer> lexerClass;
  private final Class<? extends Parser> parserClass;
  private final CommandExecutor executor;
  private final HelpCommandInfo helpCommandInfo;
  private final Map<Integer, String> rulePermissions;
  private final CompletionInfo completionInfo;
  private final Set<Integer> playerOnlyCommands;
  private final boolean useDefaultParseError;

  CommandConnectorImpl(Collection<CommandRoot> roots,
                       Class<? extends Lexer> lexerClass,
                       Class<? extends Parser> parserClass,
                       CommandExecutor executor,
                       HelpCommandInfo helpCommandInfo,
                       Map<Integer, String> rulePermissions,
                       CompletionInfo completionInfo,
                       Set<Integer> playerOnlyCommands,
                       boolean useDefaultParseError) {
    this.roots = Collections.unmodifiableCollection(roots);
    this.lexerClass = lexerClass;
    this.parserClass = parserClass;
    this.executor = executor;
    this.helpCommandInfo = helpCommandInfo;
    this.rulePermissions = rulePermissions;
    this.completionInfo = completionInfo;
    this.playerOnlyCommands = Collections.unmodifiableSet(playerOnlyCommands);
    this.useDefaultParseError = useDefaultParseError;
  }

  @Override
  public Lexer lexer(CharStream input) {
    Constructor<? extends Lexer> constructor;
    try {
      constructor = lexerClass.getDeclaredConstructor(CharStream.class);
    } catch (NoSuchMethodException e) {
      Mantle.getProxy().logger().error("The lexer class does not have the required constructor: "
          + lexerClass.getSimpleName());
      throw new InvalidCommandConnector();
    }
    try {
      return constructor.newInstance(input);
    } catch (InstantiationException e) {
      Mantle.getProxy().logger().error("The required lexer class' constructor is abstract and cannot be instantiated: "
          + lexerClass.getSimpleName());
      throw new InvalidCommandConnector();
    } catch (IllegalAccessException e) {
      Mantle.getProxy().logger().error("The required lexer class' constructor is not accessible: "
          + lexerClass.getSimpleName());
      throw new InvalidCommandConnector();
    } catch (InvocationTargetException e) {
      Mantle.getProxy().logger().error("The required lexer class' constructor threw an exception: "
          + lexerClass.getSimpleName());
      throw new InvalidCommandConnector();
    }
  }

  @Override
  public Parser parser(TokenStream tokenStream) {
    Constructor<? extends Parser> constructor;
    try {
      constructor = parserClass.getDeclaredConstructor(TokenStream.class);
    } catch (NoSuchMethodException e) {
      Mantle.getProxy().logger().error("The parser class does not have the required constructor: "
          + parserClass.getSimpleName());
      throw new InvalidCommandConnector();
    }
    Parser parser;
    try {
      parser = constructor.newInstance(tokenStream);
    } catch (InstantiationException e) {
      Mantle.getProxy().logger().error("The required parser class' constructor is abstract and cannot be instantiated: "
          + parserClass.getSimpleName());
      throw new InvalidCommandConnector();
    } catch (IllegalAccessException e) {
      Mantle.getProxy().logger().error("The required parser class' constructor is not accessible: "
          + parserClass.getSimpleName());
      throw new InvalidCommandConnector();
    } catch (InvocationTargetException e) {
      Mantle.getProxy().logger().error("The required parser class' constructor threw an exception: "
          + parserClass.getSimpleName());
      throw new InvalidCommandConnector();
    }
    return parser;
  }

  @Override
  public ParserRuleContext baseContext(Parser parser, CommandRoot root) {
    Method parserRuleContextMethod;
    try {
      parserRuleContextMethod = parserClass.getDeclaredMethod(root.baseCommand());
    } catch (NoSuchMethodException e) {
      Mantle.getProxy().logger().error("The parser class does not have the required constructor: "
          + parserClass.getSimpleName());
      throw new InvalidCommandConnector();
    }
    Object parserRuleContextObject;
    try {
      parserRuleContextObject = parserRuleContextMethod.invoke(parser);
    } catch (IllegalAccessException e) {
      Mantle.getProxy().logger().error("The required parser class' base command is not accessible: "
          + parserClass.getSimpleName()
          + ", "
          + root.baseCommand()
          + "()");
      throw new InvalidCommandConnector();
    } catch (InvocationTargetException e) {
      Mantle.getProxy().logger().error("The required parser class' base command threw an exception: "
          + parserRuleContextMethod.toGenericString());
      throw new InvalidCommandConnector();
    }
    if (!(parserRuleContextObject instanceof ParserRuleContext)) {
      Mantle.getProxy().logger().error("The required parser class' base command did not return a ParseTree: "
          + parserClass.getSimpleName()
          + ", "
          + root.baseCommand()
          + "()");
      throw new InvalidCommandConnector();
    }
    return (ParserRuleContext) parserRuleContextObject;
  }

  @Override
  public Collection<CommandRoot> roots() {
    return roots;
  }

  @Override
  public CommandExecutor executor() {
    return executor;
  }

  @Override
  public Map<Integer, String> rulePermissions() {
    return rulePermissions;
  }

  @Override
  public @Nullable HelpCommandInfo helpCommandInfo() {
    return helpCommandInfo;
  }

  @Override
  public CompletionInfo completionInfo() {
    return completionInfo;
  }

  @Override
  public Set<Integer> playerOnlyCommands() {
    return playerOnlyCommands;
  }

  @Override
  public boolean useDefaultParseError() {
    return useDefaultParseError;
  }
}
