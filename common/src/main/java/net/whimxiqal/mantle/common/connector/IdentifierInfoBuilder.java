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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.whimxiqal.mantle.common.Builder;
import net.whimxiqal.mantle.common.CommandContext;
import net.whimxiqal.mantle.common.parameter.Parameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

/**
 * A builder for {@link IdentifierInfo}.
 */
public class IdentifierInfoBuilder<T extends ParserRuleContext> implements Builder<IdentifierInfo<T>> {

  private final int rule;
  private final Class<T> clazz;
  private final Map<Integer, Map<Integer, String>> completionTable = new HashMap<>();
  private final Map<String, Parameter> parameters = new HashMap<>();
  private final Set<Integer> ignoredCompletionTokens = new HashSet<>();
  private Function<T, String> extractor;

  public IdentifierInfoBuilder(int rule, Class<T> clazz) {
    this.rule = rule;
    this.clazz = clazz;
  }

  @Override
  public IdentifierInfo<T> build() {
    if (extractor == null) {
      throw new InvalidCommandConnector("The IdentifierInfo must have a valid identifier extractor");
    }
    return new IdentifierInfoImpl<>(rule,
        clazz,
        extractor,
        completionTable,
        parameters,
        ignoredCompletionTokens);
  }

  /**
   * Set a custom extractor from the identifier parser rule context.
   * The text of the identifier will be extracted from the context during execution
   * using this function.
   *
   * @param extractor the extractor
   * @return builder, for chaining
   */
  public IdentifierInfoBuilder<T> extractor(Function<T, String> extractor) {
    this.extractor = extractor;
    return this;
  }

  /**
   * Set an extractor using the recommended way of managing identifiers in Mantle.
   * The identifier rule should be made up of multiple "smaller" rules that individually
   * handle strings of contiguous characters, which are enclosed in bounding characters like
   * quotation marks.
   * This standard extractor just requires the function to get the "smaller" internal identifier
   * rule, and does the rest internally.
   *
   * @param childContext the accessor for the internal child rule context
   * @return builder, for chaining
   */
  public IdentifierInfoBuilder<T> standardExtractor(Function<T, List<? extends ParserRuleContext>> childContext) {
    this.extractor = ctx -> childContext.apply(ctx).stream().map(RuleContext::getText).collect(Collectors.joining(" "));
    return this;
  }

  /**
   * Register a completion.
   * The caller rule is the rule containing the completable rule.
   * The completable rule is the rule that is being completed with this information.
   * Since the caller rule may have multiple instances of the completable rule, the completable index]
   * denotes the index at which this completion should take effect.
   *
   * @param callerRule      the caller rule
   * @param identifierIndex the completable rule
   * @param parameterName   the name of the parameter to use
   * @return builder, for chaining
   * @see #addParameter(Parameter)
   * @see IdentifierInfo#completeIdentifier(CommandContext, int, int)
   */
  public IdentifierInfoBuilder<T> registerCompletion(int callerRule,
                                                     int identifierIndex,
                                                     String parameterName) {
    completionTable.computeIfAbsent(callerRule, k -> new HashMap<>())
        .put(identifierIndex, parameterName);
    return this;
  }

  /**
   * Add a parameter.
   *
   * @param parameter the parameter
   * @return the builder, for chaining
   */
  public IdentifierInfoBuilder<T> addParameter(Parameter parameter) {
    parameters.put(parameter.name(), parameter);
    return this;
  }

  /**
   * Add a token that should be ignored by the completion engine.
   * A good example and a recommended one to add here is the quotation mark.
   *
   * @param token the integer of the token
   * @return the builder, for chaining
   */
  public IdentifierInfoBuilder<T> addIgnoredCompletionToken(int token) {
    if (ignoredCompletionTokens.contains(token)) {
      throw new IllegalArgumentException("Token " + token + " is already ignored.");
    }
    ignoredCompletionTokens.add(token);
    return this;
  }

}
