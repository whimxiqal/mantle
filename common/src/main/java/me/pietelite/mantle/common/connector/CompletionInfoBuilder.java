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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import me.pietelite.mantle.common.Builder;
import me.pietelite.mantle.common.CommandSource;

/**
 * A builder for {@link CompletionInfo}.
 */
public class CompletionInfoBuilder implements Builder<CompletionInfo> {

  private final Map<Integer, Map<Integer, Map<Integer, String>>> completionTable = new HashMap<>();
  private final Map<String, Function<CommandSource, Collection<String>>> parameterToCompletions = new HashMap<>();
  private final Set<Integer> ignoredCompletionTokens = new HashSet<>();

  public CompletionInfo build() {
    return new CompletionInfoImpl(completionTable, parameterToCompletions, ignoredCompletionTokens);
  }

  /**
   * Register a completion.
   * The caller rule is the rule containing the completable rule.
   * The completable rule is the rule that is being completed with this information.
   * Since the caller rule may have multiple instances of the completable rule, the completable index]
   * denotes the index at which this completion should take effect.
   *
   * @param callerRule       the caller rule
   * @param completableRule  the completable rule
   * @param completableIndex the index of the correct instance of the completable rule
   * @param parameter        the name of the parameter to use
   * @return builder, for chaining
   * @see #addParameter(String, Collection)
   * @see #addParameter(String, Function)
   * @see CompletionInfo#completionsFor(CommandSource, int, int, int)
   */
  public CompletionInfoBuilder registerCompletion(int callerRule,
                                                  int completableRule,
                                                  int completableIndex,
                                                  String parameter) {
    completionTable.computeIfAbsent(completableRule, k -> new HashMap<>())
        .computeIfAbsent(callerRule, k -> new HashMap<>())
        .put(completableIndex, parameter);
    return this;
  }

  /**
   * Add a parameter.
   *
   * @param parameterName the parameter name
   * @param completions   the completions allowed for this parameter
   * @return the builder, for chaining
   */
  public CompletionInfoBuilder addParameter(String parameterName, Collection<String> completions) {
    parameterToCompletions.put(parameterName, (src) -> completions);
    return this;
  }

  /**
   * Add a parameter.
   *
   * @param parameterName the parameter name
   * @param completions   the completions allowed for this parameter, to be evaluated at completion runtime
   * @return the builder, for chaining
   */
  public CompletionInfoBuilder addParameter(String parameterName,
                                            Function<CommandSource, Collection<String>> completions) {
    parameterToCompletions.put(parameterName, completions);
    return this;
  }

  /**
   * Add a token that should be ignored by the completion engine.
   * A good example and a recommended one to add here is the quotation mark.
   *
   * @param token the integer of the token
   * @return the builder, for chaining
   */
  public CompletionInfoBuilder addIgnoredCompletionToken(int token) {
    if (ignoredCompletionTokens.contains(token)) {
      throw new IllegalArgumentException("Token " + token + " is already ignored.");
    }
    ignoredCompletionTokens.add(token);
    return this;
  }

}
