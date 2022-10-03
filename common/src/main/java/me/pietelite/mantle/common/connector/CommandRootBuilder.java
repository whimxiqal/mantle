package me.pietelite.mantle.common.connector;

import java.util.LinkedList;
import java.util.List;
import net.kyori.adventure.text.Component;

public class CommandRootBuilder {

  private final String baseCommand;
  private Component description;
  private final List<String> aliases = new LinkedList<>();

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
