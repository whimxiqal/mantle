package me.pietelite.mantle.common;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public interface Proxy {

  @Nullable
  UUID playerUuid(String playerName);

  boolean hasPermission(UUID playerUuid, String permission);

  default boolean hasPermission(String playerName, String permission) {
    return hasPermission(playerUuid(playerName), permission);
  }

}
