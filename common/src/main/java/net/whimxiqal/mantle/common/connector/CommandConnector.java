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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.whimxiqal.mantle.common.CommandExecutor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.jetbrains.annotations.Nullable;

/**
 * A connector with which you may link your ANTLR implementation files
 * with general other command information.
 */
public interface CommandConnector {

  /**
   * Get a builder for a {@link CommandConnector}.
   *
   * @return a builder
   */
  static CommandConnectorBuilder builder() {
    return new CommandConnectorBuilder();
  }

  /**
   * The roots that each represent the base of a command tree.
   *
   * @return command roots
   */
  Collection<CommandRoot> roots();

  /**
   * The ANTLR lexer associated with all commands.
   *
   * @param input char stream
   * @return the ANTLR lexer
   */
  Lexer lexer(CharStream input);

  /**
   * The ANTLR parser associated with all commands.
   *
   * @param tokenStream token stream
   * @return the parser
   */
  Parser parser(TokenStream tokenStream);

  /**
   * The command executor, which executes command behavior.
   *
   * @return the executor
   */
  CommandExecutor executor();

  /**
   * A getter for the base parser rule context from the parser, given a certain command root.
   *
   * @param parser the parser
   * @param root   the command root
   * @return the context
   */
  ParserRuleContext baseContext(Parser parser, CommandRoot root);

  /**
   * A map of grammar rule indexes to permissions.
   * If a rule's index does not appear in this map, then the rule
   * is considered fully permissible.
   *
   * @return a map of rule indexes to permissions
   */
  Map<Integer, String> rulePermissions();

  /**
   * A set of all rule ids that represent player-only commands.
   *
   * @return player only commands
   */
  Set<Integer> playerOnlyCommands();

  /**
   * Information about identifiers, which includes user-defined parameters, command completion
   * logic, and extraction from the command parse tree.
   *
   * @return the identifier info
   */
  IdentifierInfo<?> identifierInfo();

  /**
   * True if the default parse error should be used when parsing fails.
   *
   * @return if default parser error is used
   */
  boolean useDefaultParseError();

}
