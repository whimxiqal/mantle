package me.pietelite.mantle.common;

import java.util.UUID;
import net.kyori.adventure.audience.Audience;

public class CommandSource {

  public static CommandSource unknown() {
    return new CommandSource(Type.UNKNOWN, null, Audience.empty());
  }

  public enum Type {
    CONSOLE,
    PLAYER,
    UNKNOWN
  }

  private final Type type;
  private final UUID uuid;
  private final Audience audience;

  public CommandSource(Type type, UUID uuid, Audience audience) {
    this.type = type;
    this.uuid = uuid;
    this.audience = audience;
  }

  public Type getType() {
    return type;
  }

  public UUID getUuid() {
    return uuid;
  }

  public Audience getAudience() {
    return audience;
  }
}
