package me.pietelite.mantle.common;

public class CommandSession {

  private final CommandSource source;

  public CommandSession(CommandSource source) {
    this.source = source;
  }

  public CommandSource getSource() {
    return source;
  }
}
