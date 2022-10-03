/*
 * MIT License
 *
 * Copyright (c) Pieter Svenson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.pietelite.mantle.sponge8;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import me.pietelite.mantle.common.Logger;
import me.pietelite.mantle.common.Proxy;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.util.Nameable;

class Sponge8Proxy implements Proxy {

  private final Logger logger = new Sponge8Logger();

  @Override
  public Logger logger() {
    return logger;
  }

  @Override
  public boolean hasPermission(UUID playerUuid, String permission) throws NoSuchElementException {
    Optional<ServerPlayer> player = Sponge.server().player(playerUuid);
    if (!player.isPresent()) {
      throw new NoSuchElementException("No player found with uuid " + playerUuid.toString());
    }
    return player.get().hasPermission(permission);
  }

  @Override
  public List<String> onlinePlayerNames() {
    return Sponge.server().onlinePlayers()
        .stream()
        .map(Nameable::name)
        .collect(Collectors.toList());
  }

  @Override
  public List<String> worldNames() {
    return Sponge.server().worldManager().worlds()
        .stream()
        .map(world -> world.key().value())
        .collect(Collectors.toList());
  }
}
