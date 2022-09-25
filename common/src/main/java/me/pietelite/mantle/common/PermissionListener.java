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

import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

class PermissionListener implements ParseTreeListener {

  private final Map<Integer, String> rulePermissions;
  private boolean allowed = true;

  public PermissionListener(Map<Integer, String> rulePermissions) {
    this.rulePermissions = rulePermissions;
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
    evaluatePermission(ctx.getRuleIndex(), rulePermissions);
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    // ignore
  }

  public boolean isAllowed() {
    return allowed;
  }

  private void evaluatePermission(int index, Map<Integer, String> permissionMap) {
    String permission = permissionMap.get(index);
    if (permission != null && !Mantle.sourceHasPermission(permission)) {
      allowed = false;
    }
  }
}
