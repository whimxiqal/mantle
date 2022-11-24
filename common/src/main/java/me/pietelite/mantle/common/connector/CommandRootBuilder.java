package me.pietelite.mantle.common.connector;

import java.util.LinkedList;
import java.util.List;
import me.pietelite.mantle.common.Builder;
import net.kyori.adventure.text.Component;

/**
 * A builder for a {@link CommandRoot}.
 */
public class CommandRootBuilder implements Builder<CommandRoot> {

  private final String baseCommand;
  private final List<String> aliases = new LinkedList<>();
  private Component description;

  public CommandRootBuilder(String baseCommand) {
    this.baseCommand = baseCommand;
  }

  public CommandRoot build() {
    return new CommandRootImpl(baseCommand, description, aliases);
  }

  public CommandRootBuilder description(Component description) {
    this.description = description;
    return this;
  }

  public CommandRootBuilder addAlias(String alias) {
    this.aliases.add(alias);
    return this;
  }

}
