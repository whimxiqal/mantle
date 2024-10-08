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
import java.util.Set;
import net.whimxiqal.mantle.common.CommandContext;
import net.whimxiqal.mantle.common.parameter.Parameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

/**
 * Information about how Mantle should complete partial input for dynamic parameters.
 */
public interface IdentifierInfo<T extends ParserRuleContext> {

  /**
   * Create a builder.
   *
   * @param identifierRule        the ANTLR rule for your identifiers
   * @param identifierRuleContext the context class for your identifier rule
   * @param <I>                   the parser rule context class generated by ANTLR
   * @return the builder
   */
  static <I extends ParserRuleContext> IdentifierInfoBuilder<I> builder(int identifierRule,
                                                                        Class<I> identifierRuleContext) {
    return new IdentifierInfoBuilder<>(identifierRule, identifierRuleContext);
  }

  /**
   * The ANTLR rule for your identifiers.
   *
   * @return the rule
   */
  int identifierRule();

  /**
   * The parser rule context class generated by ANTLR.
   *
   * @return the context class
   */
  Class<T> contextClass();

  /**
   * Using a parser rule context, extract an identifier.
   *
   * @param context the context
   * @return the identifier
   */
  String extractIdentifier(T context);

  /**
   * Get completions for a specific type of rule in a specific context.
   * The caller rule is the highest level rule before the rule which requires completion (completableRule).
   * The completable index is the index in the list of all completable rules for the caller rule.
   *
   * <p>Example: Say your rule is <code>"register: 'register' identifier identifier"</code>,
   * where the first identifier is the player to register and the second identifier is their favorite color.
   * You will want to complete the name with known player names and the second you will want to complete with
   * known colors. Say the register rule is rule 5 and the identifier rule is rule 8. You would then
   * get completions for the player identifier with <code>completionsFor(5, 8, 0)</code> and get
   * completions for the color identifier with <code>completionsFor(5, 8, 1)</code>.
   *
   * @param context         the command context
   * @param callerRule      the rule which is called during completion
   * @param identifierIndex the index in the list of all identifiers in the caller rule
   * @return possible completions
   */
  Collection<String> completeIdentifier(CommandContext context, int callerRule, int identifierIndex);

  /**
   * Get the parameter available at a given rule at a given identifier index.
   *
   * @param callerRule      the rule
   * @param identifierIndex the index of the identifier within in the rule
   * @return the parameter
   */
  @Nullable Parameter parameterAt(int callerRule, int identifierIndex);

  /**
   * The set of ids of tokens that should be ignored when offering completion suggestions.
   *
   * @return the tokens
   */
  @Nullable Set<Integer> ignoredCompletionTokens();

}
