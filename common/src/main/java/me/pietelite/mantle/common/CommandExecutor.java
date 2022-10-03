package me.pietelite.mantle.common;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

@FunctionalInterface
public interface CommandExecutor {

  ParseTreeVisitor<CommandResult> provide(CommandSource source);

}
