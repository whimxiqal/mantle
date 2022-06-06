package me.pietelite.mantle.common;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MantleTest {

  @BeforeAll
  static void setUpAll() {
    Mantle.setProxy(new CrustPlatformProxy());
    CrustPlugin.instance = new CrustPlugin();
    CrustPlatformConnector command = new CrustPlatformConnector();
    command.registerTo(CrustPlugin.instance);
  }

  @BeforeEach
  void setUp() {
    CrustPlugin.instance.players.clear();
    CrustPlugin.instance.playerRestrictedPermissions.clear();
  }

  CrustPlugin instance() {
    return CrustPlugin.instance;
  }

  @Test
  void completeCommand() {
    CommandSource source = new CommandSource(CommandSource.Type.CONSOLE, null, Audience.empty());
    List<String> completions = instance().completeCommand(source, "crust");
    Assertions.assertEquals(3, completions.size());
    Assertions.assertTrue(completions.contains("register"));
    Assertions.assertTrue(completions.contains("unregister"));
    Assertions.assertTrue(completions.contains("player"));

    completions = instance().completeCommand(source, "crust r");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("register"));

    completions = instance().completeCommand(source, "crust u");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("unregister"));

    completions = instance().completeCommand(source, "crust player");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("player"));

    completions = instance().completeCommand(source, "crust player golem ");
    Assertions.assertEquals(2, completions.size());
    Assertions.assertTrue(completions.contains("info"));
    Assertions.assertTrue(completions.contains("edit"));

    completions = instance().completeCommand(source, "crust z");
    Assertions.assertEquals(0, completions.size());
  }

  @Test
  void completeCommandWithPermissions() {
    UUID playerUuid = UUID.randomUUID();
    CommandSource source = new CommandSource(CommandSource.Type.PLAYER, playerUuid, Audience.empty());
    List<String> completions = instance().completeCommand(source, "crust player golem ");
    Assertions.assertEquals(2, completions.size());

    CrustPlugin.instance.revokePermission(playerUuid, "crust.player.edit");
    completions = instance().completeCommand(source, "crust player golem ");
    Assertions.assertEquals(1, completions.size());

    CrustPlugin.instance.revokePermission(playerUuid, "crust.player");
    completions = instance().completeCommand(source, "crust player golem ");
    Assertions.assertEquals(0, completions.size());
  }

  @Test
  void processCommand() {
    CommandSource source = new CommandSource(CommandSource.Type.CONSOLE, null, Audience.empty());
    Set<String> players = CrustPlugin.instance.players;
    Assertions.assertEquals(0, players.size());
    Assertions.assertTrue(instance().executeCommand(source, "crust register apollo"));
    Assertions.assertEquals(1, players.size());
    Assertions.assertTrue(players.contains("apollo"));
    Assertions.assertFalse(instance().executeCommand(source, "crust register apollo"));
    Assertions.assertEquals(1, players.size());
    Assertions.assertTrue(players.contains("apollo"));
    Assertions.assertTrue(instance().executeCommand(source, "crust register zeus"));
    Assertions.assertEquals(2, players.size());
    Assertions.assertTrue(players.contains("zeus"));
  }

  @Test
  void processCommandWithPermissions() {
    UUID playerUuid = UUID.randomUUID();
    CommandSource source = new CommandSource(CommandSource.Type.PLAYER, playerUuid, Audience.empty());
    Set<String> hosts = CrustPlugin.instance.players;
    Assertions.assertTrue(instance().executeCommand(source, "crust register ares"));
    Assertions.assertTrue(instance().executeCommand(source, "crust register hermes"));
    Assertions.assertEquals(2, hosts.size());
    Assertions.assertTrue(hosts.contains("ares"));
    Assertions.assertTrue(hosts.contains("hermes"));

    CrustPlugin.instance.revokePermission(playerUuid, "crust.register");
    Assertions.assertFalse(instance().executeCommand(source, "crust register hades"));
    Assertions.assertEquals(2, hosts.size());
    Assertions.assertTrue(hosts.contains("ares"));
    Assertions.assertTrue(hosts.contains("hermes"));

    Assertions.assertTrue(instance().executeCommand(source, "crust unregister ares"));
    Assertions.assertEquals(1, hosts.size());
    Assertions.assertTrue(hosts.contains("hermes"));

    CrustPlugin.instance.revokePermission(playerUuid, "crust.unregister");
    Assertions.assertFalse(instance().executeCommand(source, "crust register hermes"));
    Assertions.assertEquals(1, hosts.size());
    Assertions.assertTrue(hosts.contains("hermes"));

    // Make sure that permissions work on inherited nodes too
    Assertions.assertTrue(instance().executeCommand(source, "crust player ares edit nickname mars"));
    CrustPlugin.instance.revokePermission(playerUuid, "crust.player");
    Assertions.assertFalse(instance().executeCommand(source, "crust player ares edit nickname mars"));
  }

}
