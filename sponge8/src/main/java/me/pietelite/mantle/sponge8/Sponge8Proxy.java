package me.pietelite.mantle.sponge8;

import java.util.Optional;
import java.util.UUID;
import me.pietelite.mantle.common.Proxy;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.PermissionService;

public class Sponge8Proxy implements Proxy {
  @Override
  public UUID playerUuid(String playerName) {
    return Sponge.server().player(playerName).map(ServerPlayer::uniqueId).orElse(null);
  }

  @Override
  public boolean hasPermission(UUID playerUuid, String permission) {
    Optional<ServerPlayer> player = Sponge.server().player(playerUuid);
    return player.isPresent() && player.get().hasPermission(permission);
  }

  @Override
  public boolean hasPermission(String playerName, String permission) {
    Optional<ServerPlayer> player = Sponge.server().player(playerName);
    return player.isPresent() && player.get().hasPermission(permission);
  }
}
