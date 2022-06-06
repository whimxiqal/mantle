package me.pietelite.mantle.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CrustPlugin {

  public static CrustPlugin instance;
  private final Map<String, MantleCommand> commands = new HashMap<>();
  public final Set<String> players = new HashSet<>();
  public final Map<UUID, Set<String>> playerRestrictedPermissions = new HashMap<>();

  public void registerCommand(String alias, MantleCommand command) {
    commands.put(alias, command);
  }

  public boolean executeCommand(CommandSource source, String command) {
    String[] tokens = command.split(" ", 2);
    return commands.get(tokens[0]).process(source, tokens.length < 2 ? "" : tokens[1]);
  }

  public List<String> completeCommand(CommandSource source, String command) {
    String[] tokens = command.split(" ", 2);
    return commands.get(tokens[0]).complete(source, tokens.length < 2 ? "" : tokens[1]);
  }

  public void revokePermission(UUID playerUuid, String permission) {
    this.playerRestrictedPermissions.computeIfAbsent(playerUuid, k -> new HashSet<>()).add(permission);
  }

}
