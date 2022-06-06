package me.pietelite.mantle.common;

public interface MantleCommandRegistrar<C extends MantleCommand> {

  void register(C command);

}
