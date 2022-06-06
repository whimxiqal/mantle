package me.pietelite.mantle.common;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CrustPlatformProxy implements Proxy {
  @Override
  public UUID playerUuid(String playerName) {
    return UUID.nameUUIDFromBytes(playerName.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public boolean hasPermission(UUID playerUuid, String permission) {
    return !CrustPlugin.instance.playerRestrictedPermissions.containsKey(playerUuid) ||
        !CrustPlugin.instance.playerRestrictedPermissions.get(playerUuid).contains(permission);
  }
}
