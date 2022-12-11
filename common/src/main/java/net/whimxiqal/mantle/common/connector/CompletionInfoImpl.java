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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.Mantle;
import org.jetbrains.annotations.Nullable;

class CompletionInfoImpl implements CompletionInfo {

  public static final Map<String, Function<CommandSource, Collection<String>>> DEFAULT_PARAMETERS = new HashMap<>();

  static {
    DEFAULT_PARAMETERS.put("player", (src) -> Mantle.getProxy().onlinePlayerNames());
    DEFAULT_PARAMETERS.put("world", (src) -> Mantle.getProxy().worldNames());
  }


  private final Map<Integer, Map<Integer, Map<Integer, String>>> completionTable;
  private final Map<String, Function<CommandSource, Collection<String>>> parameterToCompletions;
  private final Set<Integer> ignoredCompletionTokens;

  public CompletionInfoImpl(Map<Integer, Map<Integer, Map<Integer, String>>> completionTable,
                            Map<String, Function<CommandSource, Collection<String>>> parameterToCompletions,
                            Set<Integer> ignoredCompletionTokens) {
    this.completionTable = completionTable;
    this.parameterToCompletions = parameterToCompletions;
    this.ignoredCompletionTokens = ignoredCompletionTokens;
  }

  @Override
  public Collection<String> completionsFor(CommandSource source,
                                           int callerRule,
                                           int completableRule,
                                           int completableIndex) {
    Map<Integer, Map<Integer, String>> completionTable1 = completionTable.get(completableRule);
    if (completionTable1 == null) {
      return Collections.emptyList();
    }
    Map<Integer, String> completionTable2 = completionTable1.get(callerRule);
    if (completionTable2 == null) {
      return Collections.emptyList();
    }
    String parameter = completionTable2.get(completableIndex);
    if (parameter == null) {
      return Collections.emptyList();
    }

    Function<CommandSource, Collection<String>> completions = DEFAULT_PARAMETERS.get(parameter);
    if (completions == null) {
      completions = parameterToCompletions.get(parameter);
      if (completions == null) {
        return Collections.emptyList();
      }
    }
    return completions.apply(source);
  }

  @Override
  public Set<Integer> completableRules() {
    return completionTable.keySet();
  }

  @Override
  public @Nullable Set<Integer> ignoredCompletionTokens() {
    return ignoredCompletionTokens;
  }
}
