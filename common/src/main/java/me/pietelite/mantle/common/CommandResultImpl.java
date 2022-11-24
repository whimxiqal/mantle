package me.pietelite.mantle.common;

import java.util.Optional;
import net.kyori.adventure.text.Component;

@SuppressWarnings("checkstyle:MissingJavadocType")
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

}
