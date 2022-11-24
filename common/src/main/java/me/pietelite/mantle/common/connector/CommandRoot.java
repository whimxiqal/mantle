package me.pietelite.mantle.common.connector;

import java.util.List;
import net.kyori.adventure.text.Component;

/**
 * The base of a command tree.
 */
public interface CommandRoot {

  static CommandRootBuilder builder(String baseCommand) {
    return new CommandRootBuilder(baseCommand);
  }

  /**
   * The base word of the command.
   *
   * @return the base command
   */
  String baseCommand();

  /**
   * A general description of what the command is or does.
   *
   * @return description
   */
  Component description();

  /**
   * All aliases of the command, besides the normal base command.
   *
   * @return aliases
   */
  List<String> aliases();

}
