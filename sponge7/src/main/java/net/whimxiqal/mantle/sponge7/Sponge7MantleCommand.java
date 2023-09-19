package net.whimxiqal.mantle.sponge7;/*
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

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.MantleCommand;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

final class Sponge7MantleCommand extends MantleCommand implements CommandCallable {

  private final SpongeAudiences spongeAudiences;

  public Sponge7MantleCommand(CommandConnector connector, CommandRoot root, SpongeAudiences spongeAudiences) {
    super(connector, root);
    this.spongeAudiences = spongeAudiences;
  }

  private CommandSource convertCause(org.spongepowered.api.command.CommandSource source) {
    if (source instanceof ConsoleSource) {
      return new CommandSource(CommandSource.Type.CONSOLE,
          null,
          spongeAudiences.console());
    } else if (source instanceof Player) {
      return new CommandSource(CommandSource.Type.PLAYER,
          ((Player) source).getUniqueId(),
          spongeAudiences.player((Player) source));
    }
    return CommandSource.unknown();
  }

  private CommandResult convertResult(net.whimxiqal.mantle.common.CommandResult result) {
    switch (result.type()) {
      case SUCCESS:
        return CommandResult.success();
      case FAILURE:
      default:
        return CommandResult.empty();
    }
  }

  @Override
  public @NotNull CommandResult process(org.spongepowered.api.command.@NotNull CommandSource source, @NotNull String arguments) {
    return convertResult(super.process(convertCause(source), arguments));
  }

  @Override
  public @NotNull List<String> getSuggestions(org.spongepowered.api.command.@NotNull CommandSource source, @NotNull String arguments, @Nullable Location<World> targetPosition) {
    return complete(convertCause(source), arguments);
  }

  @Override
  public boolean testPermission(org.spongepowered.api.command.@NotNull CommandSource source) {
    return true;
  }

  @Override
  public @NotNull Optional<Text> getShortDescription(org.spongepowered.api.command.@NotNull CommandSource source) {
    return Optional.empty();
  }

  @Override
  public @NotNull Optional<Text> getHelp(org.spongepowered.api.command.@NotNull CommandSource source) {
    return Optional.empty();
  }

  @Override
  public @NotNull Text getUsage(org.spongepowered.api.command.@NotNull CommandSource source) {
    return Text.EMPTY;
  }
}
