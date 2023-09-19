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

import net.whimxiqal.mantle.common.CommandRegistrar;
import net.whimxiqal.mantle.common.Mantle;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

/**
 * A static provider for a {@link CommandRegistrar} for Sponge API 8.
 */
public class Sponge9RegistrarProvider {

  /**
   * Get a {@link CommandRegistrar} for Sponge API 8 plugins.
   *
   * @param pluginContainer         the plugin container
   * @param rawRegisterCommandEvent the raw command event with which commands are normally registered
   * @return the registrar
   */
  public static CommandRegistrar get(PluginContainer pluginContainer,
                                     RegisterCommandEvent<Command.Raw> rawRegisterCommandEvent) {
    Mantle.setProxy(new Sponge9Proxy());
    return new Sponge9CommandRegistrar(pluginContainer, rawRegisterCommandEvent);
  }

}
