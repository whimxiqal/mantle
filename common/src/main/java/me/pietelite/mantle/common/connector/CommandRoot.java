package me.pietelite.mantle.common.connector;

import java.util.List;
import net.kyori.adventure.text.Component;

public interface CommandRoot {

  static CommandRootBuilder builder(String baseCommand) {
    return new CommandRootBuilder(baseCommand);
  }

  String baseCommand();

  Component description();

  List<String> aliases();

}
