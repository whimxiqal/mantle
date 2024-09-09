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

package net.whimxiqal.mantle.common.phase;

import java.util.Stack;
import net.whimxiqal.mantle.common.IdentifierTrackerImpl;
import net.whimxiqal.mantle.common.connector.IdentifierInfo;
import net.whimxiqal.mantle.common.parameter.Parameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * A parse tree listener for finding identifiers.
 */
class IdentifierListener implements ParseTreeListener {

  private final IdentifierInfo<?> info;
  private final IdentifierTrackerImpl tracker;
  private final Stack<IdentifierCount> ruleStack = new Stack<>();
  private final boolean validate;
  private Parameter invalid;

  public IdentifierListener(IdentifierInfo<?> info, IdentifierTrackerImpl tracker, boolean validate) {
    this.info = info;
    this.tracker = tracker;
    this.validate = validate;
    this.invalid = null;
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    // ignore
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    // ignore
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    enter(ctx, info);
    ruleStack.push(new IdentifierCount(ctx.getRuleIndex()));
  }

  private <T extends ParserRuleContext> void enter(ParserRuleContext ctx, IdentifierInfo<T> info) {
    if (invalid != null) {
      return;
    }
    if (!info.contextClass().isInstance(ctx)) {
      return;
    }
    Parameter parameter = info.parameterAt(ruleStack.peek().rule, ruleStack.peek().count);
    String identifier = info.extractIdentifier(info.contextClass().cast(ctx));
    if (parameter == null) {
      tracker.add(null, identifier);
    } else {
      if (!validate || parameter.isValid(identifier)) {
        tracker.add(parameter.name(), identifier);
      } else {
        invalid = parameter;
      }
    }
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    ruleStack.pop();
    if (ctx.getRuleIndex() == info.identifierRule()) {
      // we just left the identifier, so this rule is now potentially on it's next identifier
      ruleStack.peek().count++;
    }
  }

  /**
   * The parameter that had invalid input.
   *
   * @return the parameter
   */
  public Parameter getInvalid() {
    return invalid;
  }

  private static class IdentifierCount {
    int rule;
    int count;

    IdentifierCount(int rule) {
      this.rule = rule;
      this.count = 0;
    }
  }

}
