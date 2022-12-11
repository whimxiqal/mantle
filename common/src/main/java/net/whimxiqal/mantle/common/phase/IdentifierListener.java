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

import java.util.Objects;
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
public class IdentifierListener implements ParseTreeListener {

  private final IdentifierInfo<?> info;
  private final IdentifierTrackerImpl tracker;
  private final Stack<IdentifierCount> ruleStack = new Stack<>();

  private static class IdentifierCount {
    IdentifierCount(int rule) {
      this.rule = rule;
      this.count = 0;
    }

    int rule;
    int count;
  }

  public IdentifierListener(IdentifierInfo<?> info, IdentifierTrackerImpl tracker) {
    this.info = info;
    this.tracker = tracker;
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
    if (info.contextClass().isInstance(ctx)) {
      Parameter parameter = info.parameterAt(ruleStack.peek().rule, ruleStack.peek().count);
      tracker.add(parameter == null ? null : parameter.name(),
          info.extractIdentifier(info.contextClass().cast(ctx)));
    }
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    boolean isIdentifier = ctx.getRuleIndex() == info.identifierRule();
    ruleStack.pop();
    if (isIdentifier) {
      // we just left the identifier, so this rule is now potentially on it's next identifier
      ruleStack.peek().count++;
    }
  }

}
