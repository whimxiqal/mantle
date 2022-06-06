package me.pietelite.mantle.sponge8;

import java.util.List;
import me.pietelite.mantle.common.MantleCommandRegistrar;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

public class Sponge8MantleCommandRegistrar implements MantleCommandRegistrar<Sponge8MantleCommand> {

  private final PluginContainer pluginContainer;
  private final RegisterCommandEvent<Command.Raw> rawRegisterCommandEvent;

  public Sponge8MantleCommandRegistrar(PluginContainer pluginContainer, RegisterCommandEvent<Command.Raw> rawRegisterCommandEvent) {
    this.pluginContainer = pluginContainer;
    this.rawRegisterCommandEvent = rawRegisterCommandEvent;
  }

  @Override
  public void register(Sponge8MantleCommand command) {
    List<String> aliases = command.getConnector().aliases();
    String alias = aliases.get(0);
    String[] otherAliases;
    if (aliases.size() > 1) {
      otherAliases = new String[aliases.size() - 1];
      for (int i = 0; i < aliases.size() - 1; i++) {
        otherAliases[i] = aliases.get(i + 1);
      }
    } else {
      otherAliases = new String[0];
    }
    rawRegisterCommandEvent.register(pluginContainer, command, alias, otherAliases);
  }

}
