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

package net.whimxiqal.mantle.paper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import net.whimxiqal.mantle.common.Logger;
import net.whimxiqal.mantle.common.Proxy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class PaperProxy implements Proxy {

  private final Logger logger;

  PaperProxy(Plugin plugin) {
    java.util.logging.Logger baseLogger = java.util.logging.Logger.getLogger("Mantle");
    baseLogger.setParent(plugin.getLogger());
    this.logger = new PaperLogger(baseLogger);
  }

  @Override
  public Logger logger() {
    return logger;
  }

  @Override
  public boolean hasPermission(UUID playerUuid, String permission) throws NoSuchElementException {
    Player player = Bukkit.getPlayer(playerUuid);
    if (player == null) {
      throw new NoSuchElementException("No player found with uuid " + playerUuid);
    }
    return player.hasPermission(permission);
  }

  @Override
  public List<String> onlinePlayerNames() {
    return Bukkit.getServer().getOnlinePlayers()
        .stream()
        .map(HumanEntity::getName)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isOnlinePlayer(String candidate) {
    return Bukkit.getServer().getOnlinePlayers()
        .stream()
        .anyMatch(player -> player.getName().equalsIgnoreCase(candidate));
  }

  @Override
  public List<String> worldNames() {
    return Bukkit.getServer().getWorlds()
        .stream()
        .map(World::getName)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isWorldName(String candidate) {
    return Bukkit.getServer().getWorlds()
        .stream()
        .anyMatch(world -> world.getName().equalsIgnoreCase(candidate));
  }

}
