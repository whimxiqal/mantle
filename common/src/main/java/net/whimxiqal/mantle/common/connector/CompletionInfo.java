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
import java.util.Set;
import net.whimxiqal.mantle.common.CommandSource;
import org.jetbrains.annotations.Nullable;

/**
 * Information about how Mantle should complete partial input for dynamic parameters.
 */
public interface CompletionInfo {

  static CompletionInfoBuilder builder() {
    return new CompletionInfoBuilder();
  }

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
   * @param source           the command source
   * @param callerRule       the rule which is called during completion
   * @param completableRule  the rule which is being completed
   * @param completableIndex the index in the list of completable rules
   * @return possible completions
   */
  Collection<String> completionsFor(CommandSource source, int callerRule, int completableRule, int completableIndex);

  Set<Integer> completableRules();

  @Nullable Set<Integer> ignoredCompletionTokens();

}
