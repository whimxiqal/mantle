package me.pietelite.mantle.sponge8;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.pietelite.mantle.common.CommandConnector;
import me.pietelite.mantle.common.CommandSource;
import me.pietelite.mantle.common.MantleCommand;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public abstract class Sponge8MantleCommand extends MantleCommand implements Command.Raw {

  public Sponge8MantleCommand(CommandConnector connector) {
    super(connector);
  }

  @Override
  public CommandResult process(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
    cause.audience().sendMessage(Component.text(process(convertCause(cause), arguments.remaining())));
    return CommandResult.success();
  }

  @Override
  public List<CommandCompletion> complete(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
    return complete(convertCause(cause), arguments.remaining()).stream()
        .map(CommandCompletion::of)
        .collect(Collectors.toList());
  }

  @Override
  public boolean canExecute(CommandCause cause) {
    return true;
  }

  @Override
  public Optional<Component> shortDescription(CommandCause cause) {
    return Optional.empty();
  }

  @Override
  public Optional<Component> extendedDescription(CommandCause cause) {
    return Optional.empty();
  }

  @Override
  public Component usage(CommandCause cause) {
    return Component.empty();
  }

  private CommandSource convertCause(CommandCause cause) {
    if (cause.root() instanceof Server) {
      return new CommandSource(CommandSource.Type.CONSOLE, null, (Server) cause.root());
    } else if (cause.root() instanceof ServerPlayer) {
      return new CommandSource(CommandSource.Type.PLAYER, ((ServerPlayer) cause.root()).uniqueId(), (ServerPlayer) cause.root());
    }
    return CommandSource.unknown();
  }
}
