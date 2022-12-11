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

package net.whimxiqal.mantle.sponge8;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.MantleCommand;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

final class Sponge8MantleCommand extends MantleCommand implements Command.Raw {

  public Sponge8MantleCommand(CommandConnector connector, CommandRoot root) {
    super(connector, root);
  }

  @Override
  public CommandResult process(CommandCause cause, ArgumentReader.Mutable arguments) {
    return CommandResult.success();
  }

  @Override
  public List<CommandCompletion> complete(CommandCause cause, ArgumentReader.Mutable arguments) {
    return complete(convertCause(cause), arguments.remaining()).stream()
        .map(CommandCompletion::of)
        .collect(Collectors.toList());
  }

  @Override
  public boolean canExecute(CommandCause cause) {
    return true;
  }

  @Override
  public Optional<Component> shortDescription(CommandCause cause) {
    return Optional.empty();
  }

  @Override
  public Optional<Component> extendedDescription(CommandCause cause) {
    return Optional.empty();
  }

  @Override
  public Component usage(CommandCause cause) {
    return Component.empty();
  }

  private CommandSource convertCause(CommandCause cause) {
    if (cause.root() instanceof Server) {
      return new CommandSource(CommandSource.Type.CONSOLE,
          null,
          (Server) cause.root());
    } else if (cause.root() instanceof ServerPlayer) {
      return new CommandSource(CommandSource.Type.PLAYER,
          ((ServerPlayer) cause.root()).uniqueId(),
          (ServerPlayer) cause.root());
    }
    return CommandSource.unknown();
  }
}
