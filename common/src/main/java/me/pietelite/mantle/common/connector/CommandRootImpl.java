package me.pietelite.mantle.common.connector;

import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class CommandRootImpl implements CommandRoot {

  private final String baseCommand;
  private final Component description;
  private final List<String> aliases;

  CommandRootImpl(String baseCommand,
                  Component description,
                  List<String> aliases) {
    this.baseCommand = baseCommand;
    this.description = description;
    this.aliases = Collections.unmodifiableList(aliases);
  }

  @Override
  public String baseCommand() {
    return baseCommand;
  }

  @Override
  public Component description() {
    return description;
  }

  @Override
  public List<String> aliases() {
    return aliases;
  }

}
