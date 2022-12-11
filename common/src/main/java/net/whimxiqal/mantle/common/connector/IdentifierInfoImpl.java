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
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.whimxiqal.mantle.common.CommandContext;
import net.whimxiqal.mantle.common.parameter.IntegerParameter;
import net.whimxiqal.mantle.common.parameter.OnlinePlayerParameter;
import net.whimxiqal.mantle.common.parameter.Parameter;
import net.whimxiqal.mantle.common.parameter.WorldNameParameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.Nullable;

class IdentifierInfoImpl<T extends ParserRuleContext> implements IdentifierInfo<T> {

  public static final Parameter[] DEFAULT_PARAMETERS = {
      new IntegerParameter(),
      new OnlinePlayerParameter(),
      new WorldNameParameter(),
  };

  private final int rule;
  private final Class<T> clazz;
  private final Function<T, String> extractor;
  private final Map<Integer, Map<Integer, String>> parameterNameTable;
  private final Map<String, Parameter> parameters;
  private final Set<Integer> ignoredCompletionTokens;

  public IdentifierInfoImpl(int rule, Class<T> clazz, Function<T, String> extractor,
                            Map<Integer, Map<Integer, String>> parameterNameTable,
                            Map<String, Parameter> parameters,
                            Set<Integer> ignoredCompletionTokens) {
    this.rule = rule;
    this.clazz = clazz;
    this.extractor = extractor;
    this.parameterNameTable = parameterNameTable;
    this.parameters = parameters;
    this.ignoredCompletionTokens = ignoredCompletionTokens;

    for (Parameter defaultParameter : DEFAULT_PARAMETERS) {
      if (!parameters.containsKey(defaultParameter.name())) {
        parameters.put(defaultParameter.name(), defaultParameter);
      }
    }
  }

  @Override
  public int identifierRule() {
    return rule;
  }

  @Override
  public Class<T> contextClass() {
    return clazz;
  }

  @Override
  public String extractIdentifier(T context) {
    return extractor.apply(context);
  }

  @Override
  public Collection<String> completeIdentifier(CommandContext context,
                                               int callerRule,
                                               int identifierIndex) {
    Parameter parameter = parameterAt(callerRule, identifierIndex);
    if (parameter == null) {
      return Collections.emptyList();
    }
    return parameter.options().get(context);
  }

  @Override
  @Nullable
  public Parameter parameterAt(int callerRule, int identifierIndex) {
    Map<Integer, String> completionTable1 = parameterNameTable.get(identifierIndex);
    if (completionTable1 == null) {
      return null;
    }
    String parameterName = completionTable1.get(callerRule);
    return parameters.get(parameterName);
  }

  @Override
  public @Nullable Set<Integer> ignoredCompletionTokens() {
    return ignoredCompletionTokens;
  }
}
