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

import java.lang.reflect.Field;
import java.util.Locale;
import net.whimxiqal.mantle.common.CommandRegistrar;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

class PaperCommandRegistrar implements CommandRegistrar {

  private final Plugin plugin;
  private final CommandMap asyncCommandMap;
  private CommandMap commandMap;

  public PaperCommandRegistrar(Plugin plugin) {
    this.plugin = plugin;
    this.asyncCommandMap = new SimpleCommandMap(Bukkit.getServer());
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
    // add to server's existing command map for normal execution
    for (CommandRoot root : connector.roots()) {
      PaperMantleCommand command = new PaperMantleCommand(connector, root);
      commandMap.register(root.baseCommand(), command);
      for (String alias : root.aliases()) {
        commandMap.register(alias, plugin.getName().toLowerCase(Locale.ENGLISH), command);
      }
    }
    // add to custom async map for async executions
    synchronized (asyncCommandMap) {
      for (CommandRoot root : connector.roots()) {
        PaperMantleCommand asyncCommand = new PaperMantleCommand(connector, root);
        asyncCommandMap.register(root.baseCommand(), asyncCommand);
        for (String alias : root.aliases()) {
          asyncCommandMap.register(alias, plugin.getName().toLowerCase(Locale.ENGLISH), asyncCommand);
        }
      }
    }
  }

}
