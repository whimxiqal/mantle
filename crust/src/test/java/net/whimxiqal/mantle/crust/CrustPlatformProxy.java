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

package net.whimxiqal.mantle.crust;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import net.whimxiqal.mantle.common.Logger;
import net.whimxiqal.mantle.common.Proxy;

public class CrustPlatformProxy implements Proxy {

  private final CrustState state;

  public CrustPlatformProxy(CrustState state) {
    this.state = state;
  }

  @Override
  public Logger logger() {
    return new TestLogger();
  }

  @Override
  public boolean hasPermission(UUID playerUuid, String permission) {
    return !CrustPlugin.instance.playerRestrictedPermissions.containsKey(playerUuid) ||
        !CrustPlugin.instance.playerRestrictedPermissions.get(playerUuid).contains(permission);
  }

  @Override
  public List<String> onlinePlayerNames() {
    return new LinkedList<>(state.players);
  }

  @Override
  public boolean isOnlinePlayer(String candidate) {
    return state.players.contains(candidate);
  }

  @Override
  public List<String> worldNames() {
    return Collections.emptyList();
  }

  @Override
  public boolean isWorldName(String candidate) {
    return false;
  }
}
