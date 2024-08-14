package net.whimxiqal.mantle.crust;

import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.CommandContext;
import net.whimxiqal.mantle.common.CommandResult;
import net.whimxiqal.mantle.common.parameter.Parameters;

public class CrustVisitorImpl extends CrustBaseVisitor<CommandResult> {

  private final CrustState state;
  private final CommandContext context;

  public CrustVisitorImpl(CrustState state, CommandContext context) {
    this.state = state;
    this.context = context;
  }

  @Override
  public CommandResult visitRegister(CrustParser.RegisterContext ctx) {
    String playerName1 = context.identifiers().get(0);
    String playerName2 = ctx.name.getText();
    assert(playerName1.equals(playerName2));
    boolean added = state.players.add(playerName1);
    if (added) {
      context.source().audience().sendMessage(Component.text("The player was added"));
      return CommandResult.success();
    } else {
      context.source().audience().sendMessage(Component.text("A player already exists with that name"));
      return CommandResult.failure();
    }
  }

  @Override
  public CommandResult visitUnregister(CrustParser.UnregisterContext ctx) {
    String playerName1 = context.identifiers().get(Parameters.PLAYER, 0);
    String playerName2 = ctx.name.getText();
    assert(playerName1.equals(playerName2));
    boolean removed = state.players.remove(playerName1);
    if (removed) {
      context.source().audience().sendMessage(Component.text("The player was removed"));
      return CommandResult.success();
    } else {
      context.source().audience().sendMessage(Component.text("The player could not be removed"));
      return CommandResult.failure();
    }
  }

  @Override
  public CommandResult visitPlayerEditNickname(CrustParser.PlayerEditNicknameContext ctx) {
    return CommandResult.success();
  }

  @Override
  public CommandResult visitAge(CrustParser.AgeContext ctx) {
    int age = Integer.parseInt(context.identifiers().get(Parameters.INTEGER, 0));
    context.source().audience().sendMessage(Component.text("You are " + age + " years old!"));
    return CommandResult.success();
  }
}
