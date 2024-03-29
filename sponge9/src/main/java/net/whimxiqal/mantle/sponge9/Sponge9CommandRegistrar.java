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

package net.whimxiqal.mantle.sponge9;

import java.util.List;
import net.whimxiqal.mantle.common.CommandRegistrar;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

class Sponge9CommandRegistrar implements CommandRegistrar {

  private final PluginContainer pluginContainer;
  private final RegisterCommandEvent<Command.Raw> rawRegisterCommandEvent;

  public Sponge9CommandRegistrar(PluginContainer pluginContainer,
                                 RegisterCommandEvent<Command.Raw> rawRegisterCommandEvent) {
    this.pluginContainer = pluginContainer;
    this.rawRegisterCommandEvent = rawRegisterCommandEvent;
  }

  @Override
  public void register(CommandConnector connector) {
    for (CommandRoot root : connector.roots()) {
      Sponge9MantleCommand command = new Sponge9MantleCommand(connector, root);
      List<String> aliases = root.aliases();
      String[] otherAliases = new String[0];
      if (aliases != null) {
        otherAliases = aliases.toArray(otherAliases);
      }
      rawRegisterCommandEvent.register(pluginContainer, command,
          root.baseCommand(),
          otherAliases);
    }
  }

}
