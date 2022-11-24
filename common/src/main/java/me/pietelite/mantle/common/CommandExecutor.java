package me.pietelite.mantle.common;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * An executor that runs some arbitrary code whenever a {@link CommandSource} sends a valid command.
 */
@FunctionalInterface
public interface CommandExecutor {

  /**
   * Provide a suitable parse tree visitor, like one implementing an abstract class
   * given by ANTLR, given a {@link CommandSource} that is executing the command.
   *
   * @param source the source of the command
   * @return the visitor
   */
  ParseTreeVisitor<CommandResult> provide(CommandSource source);

}
