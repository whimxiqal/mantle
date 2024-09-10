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

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.MantleCommand;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
final class PaperMantleCommand implements BasicCommand {

  private final MantleCommand mantleCommand;

  public PaperMantleCommand(CommandConnector connector, CommandRoot root) {
    this.mantleCommand = new MantleCommand(connector, root);
  }

  public static CommandSource convertSender(CommandSender sender) {
    if (sender instanceof Player) {
      UUID uuid = ((Player) sender).getUniqueId();
      return new CommandSource(CommandSource.Type.PLAYER, uuid, sender);
    } else if (sender instanceof ConsoleCommandSender) {
      return new CommandSource(CommandSource.Type.CONSOLE, null, sender);
    }
    return CommandSource.unknown();
  }

  @Override
  public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
    mantleCommand.process(convertSender(commandSourceStack.getSender()), String.join(" ", Arrays.asList(args)));
  }

  @Override
  public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
    return mantleCommand.complete(convertSender(commandSourceStack.getSender()), String.join(" ", Arrays.asList(args)));
  }
}
