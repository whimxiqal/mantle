package me.pietelite.mantle.common;

import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PermissionListener implements ParseTreeListener {

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
