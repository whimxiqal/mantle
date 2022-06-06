package me.pietelite.mantle.common;

import net.kyori.adventure.text.Component;

public class CrustCommandExecutor extends CrustBaseVisitor<Boolean> {

  @Override
  public Boolean visitRegister(CrustParser.RegisterContext ctx) {
    CommandSource source = Mantle.session().getSource();
    String playerName = ctx.identifier().getText();
    boolean added = CrustPlugin.instance.players.add(playerName);
    if (added) {
      source.getAudience().sendMessage(Component.text("The player was added"));
      return true;
    } else {
      source.getAudience().sendMessage(Component.text("A player already exists with that name"));
      return false;
    }
  }

  @Override
  public Boolean visitUnregister(CrustParser.UnregisterContext ctx) {
    CommandSource source = Mantle.session().getSource();
    boolean removed = CrustPlugin.instance.players.remove(ctx.identifier().getText());
    if (removed) {
      source.getAudience().sendMessage(Component.text("The player was removed"));
      return true;
    } else {
      source.getAudience().sendMessage(Component.text("The player could not be removed"));
      return false;
    }
  }

  @Override
  public Boolean visitPlayerEditNickname(CrustParser.PlayerEditNicknameContext ctx) {
    return true;
  }
}
