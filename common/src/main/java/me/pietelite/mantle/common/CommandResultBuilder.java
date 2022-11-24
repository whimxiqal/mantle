package me.pietelite.mantle.common;

import net.kyori.adventure.text.Component;

/**
 * A generic builder for a {@link CommandResult}.
 */
public class CommandResultBuilder {

  private CommandResult.Type type;
  private Component message;

  CommandResultBuilder type(CommandResult.Type type) {
    this.type = type;
    return this;
  }

  CommandResultBuilder message(Component message) {
    this.message = message;
    return this;
  }

  CommandResult build() {
    return new CommandResultImpl(type, message);
  }

}
