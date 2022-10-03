package me.pietelite.mantle.common;

import java.util.Optional;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;

public class CommandResultImpl implements CommandResult {

  private final Type type;
  private final Component message;

  public CommandResultImpl(Type type, Component message) {
    this.type = type;
    this.message = message;
  }

  @Override
  public Type type() {
    return type;
  }

  @Override
  public Optional<Component> message() {
    return Optional.ofNullable(message);
  }

  @Override
  public CommandResult computeIfEmpty(Supplier<CommandResult> supplier) {
    if (type == Type.EMPTY) {
      return supplier.get();
    }
    return this;
  }
}
