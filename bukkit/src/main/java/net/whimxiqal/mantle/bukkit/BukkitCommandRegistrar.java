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

package net.whimxiqal.mantle.bukkit;

import java.lang.reflect.Field;
import net.whimxiqal.mantle.common.CommandRegistrar;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

class BukkitCommandRegistrar implements CommandRegistrar {

  private CommandMap commandMap;

  public BukkitCommandRegistrar() {
    Field commandMapField;
    try {
      commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      return;
    }
    commandMapField.setAccessible(true);
    try {
      commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void register(CommandConnector connector) {
    if (commandMap == null) {
      throw new RuntimeException("Bukkit commandMap could not be found");
    }
    for (CommandRoot root : connector.roots()) {
      commandMap.register(root.baseCommand(), new BukkitMantleCommand(connector, root));
      for (String alias : root.aliases()) {
        commandMap.register(alias, root.baseCommand(), new BukkitMantleCommand(connector, root));
      }
    }
  }

}
