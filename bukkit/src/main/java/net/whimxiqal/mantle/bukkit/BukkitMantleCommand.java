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

package net.whimxiqal.mantle.bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.whimxiqal.mantle.common.CommandResult;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.Mantle;
import net.whimxiqal.mantle.common.MantleCommand;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

final class BukkitMantleCommand extends Command {

  private final MantleCommand mantleCommand;
  private final CommandRoot root;
  private final LegacyComponentSerializer componentSerializer = LegacyComponentSerializer.legacyAmpersand();

  public BukkitMantleCommand(CommandConnector connector, CommandRoot root) {
    super(root.baseCommand());
    this.mantleCommand = new MantleCommand(connector, root);
    this.root = root;
  }

  @Override
  public String getName() {
    return root.baseCommand();
  }

  @Override
  public String getDescription() {
    return componentSerializer.serialize(root.description());
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    return mantleCommand.process(convertSender(sender), String.join(" ", Arrays.asList(args))).type()
        == CommandResult.Type.SUCCESS;
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
    return mantleCommand.complete(convertSender(sender), String.join(" ", Arrays.asList(args)));
  }

  private CommandSource convertSender(CommandSender sender) {
    if (sender instanceof Player) {
      UUID uuid = ((Player) sender).getUniqueId();
      return new CommandSource(CommandSource.Type.PLAYER, uuid,
          ((BukkitProxy) Mantle.getProxy()).adventure().sender(sender));
    } else if (sender instanceof ConsoleCommandSender) {
      return new CommandSource(CommandSource.Type.CONSOLE, null,
          ((BukkitProxy) Mantle.getProxy()).adventure().sender(sender));
    }
    return CommandSource.unknown();
  }

}
