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

package me.pietelite.mantle.common;

import java.util.Optional;
import java.util.Stack;
import me.pietelite.mantle.common.connector.HelpCommandInfo;
import net.kyori.adventure.text.Component;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class DescriptionListener implements ParseTreeListener {

  private final HelpCommandInfo info;
  private final Stack<Integer> ruleStack = new Stack<>();

  public DescriptionListener(HelpCommandInfo info) {
    this.info = info;
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
    if (ctx == null) {
      return;
    }
    ruleStack.push(ctx.getRuleIndex());
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    // ignore
  }

  Optional<Component> description() {
    while (!ruleStack.isEmpty()) {
      int rule = ruleStack.pop();
      if (info.ignored().contains(rule)) {
        continue;
      }
      return Optional.ofNullable(info.descriptions().get(rule));
    }
    return Optional.empty();
  }
}
