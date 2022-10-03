package me.pietelite.mantle.common;

import java.util.Optional;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;

public interface CommandResult {

  enum Type {
    EMPTY,
    SUCCESS,
    FAILURE
  }

  Type type();

  Optional<Component> message();

  CommandResult computeIfEmpty(Supplier<CommandResult> supplier);

  static CommandResultBuilder builder() {
    return new CommandResultBuilder();
  }

  static CommandResult success() {
    return builder().type(Type.SUCCESS).build();
  }

  static CommandResult failure() {
    return builder().type(Type.FAILURE).build();
  }

  static CommandResult failure(Component message) {
    return builder().type(Type.FAILURE).message(message).build();
  }

}
