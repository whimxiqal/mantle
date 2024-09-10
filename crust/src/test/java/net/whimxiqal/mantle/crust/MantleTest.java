/*
 * MIT License
 *
 * Copyright (c) Pieter Svenson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.whimxiqal.mantle.crust;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.CommandResult;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.Mantle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MantleTest {

  private final CrustState state = new CrustState();

  @BeforeEach
  void setUp() {
    Mantle.setProxy(new CrustPlatformProxy(state));
    CrustPlugin.instance = new CrustPlugin();
    CrustPlugin.instance.registerCommand(CrustConnector.generate(context -> new CrustVisitorImpl(state, context)));
    CrustPlugin.instance.playerRestrictedPermissions.clear();
  }

  CrustPlugin instance() {
    return CrustPlugin.instance;
  }

  void assertSuccess(CommandResult result) {
    Assertions.assertEquals(CommandResult.Type.SUCCESS, result.type());
  }

  void assertFailure(CommandResult result) {
    Assertions.assertEquals(CommandResult.Type.FAILURE, result.type());
  }

  @Test
  void completeCommand() {
    state.players.add("whimxiqal");
    state.players.add("belkar1");

    CommandSource source = new CommandSource(CommandSource.Type.CONSOLE, null, new TestAudience());
    List<String> completions;
    completions = instance().completeCommand(source, "crust");
    Assertions.assertEquals(4, completions.size());
    Assertions.assertTrue(completions.contains("register"));
    Assertions.assertTrue(completions.contains("unregister"));
    Assertions.assertTrue(completions.contains("player"));
    Assertions.assertTrue(completions.contains("age"));

    completions = instance().completeCommand(source, "crust r");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("register"));

    completions = instance().completeCommand(source, "crust u");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("unregister"));

    completions = instance().completeCommand(source, "crust player");
    Assertions.assertEquals(1, completions.size(), "List was not size 1: " + completions);
    Assertions.assertTrue(completions.contains("player"));

    completions = instance().completeCommand(source, "crust player ");
    Assertions.assertEquals(state.players.size(), completions.size());
    for (String player : state.players) {
      Assertions.assertTrue(completions.contains(player));
    }

    completions = instance().completeCommand(source, "crust player w");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("whimxiqal"));

    completions = instance().completeCommand(source, "crust player W");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("whimxiqal"));

    completions = instance().completeCommand(source, "crust player golem ");
    Assertions.assertEquals(2, completions.size(), completions.toString());
    Assertions.assertTrue(completions.contains("info"));
    Assertions.assertTrue(completions.contains("edit"));

    completions = instance().completeCommand(source, "crust z");
    Assertions.assertEquals(0, completions.size());

    completions = instance().completeCommand(source, "crust register tornado ");
    Assertions.assertEquals(CrustConnector.COLOR_COMPLETIONS.length, completions.size());
    for (String color : CrustConnector.COLOR_COMPLETIONS) {
      Assertions.assertTrue(completions.contains(color));
    }

    completions = instance().completeCommand(source, "crust register a b c ");
    Assertions.assertEquals(state.players.size(), completions.size());
    for (String player : state.players) {
      Assertions.assertTrue(completions.contains(player));
    }

    completions = instance().completeCommand(source, "crust player name edit color ");
    Assertions.assertEquals(CrustConnector.COLOR_COMPLETIONS.length, completions.size());
    for (String color : CrustConnector.COLOR_COMPLETIONS) {
      Assertions.assertTrue(completions.contains(color));
    }

    completions = instance().completeCommand(source, "crust player name edit color b");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertEquals("blue", completions.get(0));

    completions = instance().completeCommand(source, "crust player name edit color blue ");
    Assertions.assertTrue(completions.isEmpty());

  }

  @Test
  void completeCoreCommand() {
    CommandSource source = new CommandSource(CommandSource.Type.CONSOLE, null, new TestAudience());
    List<String> completions = instance().completeCommand(source, "core ");
    Assertions.assertEquals(CrustConnector.COLOR_COMPLETIONS.length, completions.size());
    for (String color : CrustConnector.COLOR_COMPLETIONS) {
      Assertions.assertTrue(completions.contains(color));
    }
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
    CommandSource source = new CommandSource(CommandSource.Type.CONSOLE, null, new TestAudience());
    Set<String> players = state.players;
    players.clear();
    assertSuccess(instance().executeCommand(source, "crust register apollo"));
    Assertions.assertEquals(1, players.size());
    Assertions.assertTrue(players.contains("apollo"));
    assertFailure(instance().executeCommand(source, "crust register apollo"));
    Assertions.assertEquals(1, players.size());
    Assertions.assertTrue(players.contains("apollo"));
    assertSuccess(instance().executeCommand(source, "crust register zeus"));
    Assertions.assertEquals(2, players.size());
    Assertions.assertTrue(players.contains("zeus"));

    // needs another parameter
    assertFailure(instance().executeCommand(source, "crust register"));
  }

  @Test
  void processCommandWithPermissions() {
    UUID playerUuid = UUID.randomUUID();
    CommandSource source = new CommandSource(CommandSource.Type.PLAYER, playerUuid, new TestAudience());
    Set<String> hosts = state.players;
    // starts with two

    assertSuccess(instance().executeCommand(source, "crust register ares"));
    assertSuccess(instance().executeCommand(source, "crust register hermes"));
    Assertions.assertEquals(2, hosts.size());
    Assertions.assertTrue(hosts.contains("ares"));
    Assertions.assertTrue(hosts.contains("hermes"));

    CrustPlugin.instance.revokePermission(playerUuid, "crust.register");
    assertFailure(instance().executeCommand(source, "crust register hades"));
    Assertions.assertEquals(2, hosts.size());
    Assertions.assertTrue(hosts.contains("ares"));
    Assertions.assertTrue(hosts.contains("hermes"));

    assertSuccess(instance().executeCommand(source, "crust unregister ares"));
    Assertions.assertEquals(1, hosts.size());
    Assertions.assertTrue(hosts.contains("hermes"));

    CrustPlugin.instance.revokePermission(playerUuid, "crust.unregister");
    assertFailure(instance().executeCommand(source, "crust register hermes"));
    Assertions.assertEquals(1, hosts.size());
    Assertions.assertTrue(hosts.contains("hermes"));

    // Make sure that permissions work on inherited nodes too
    assertSuccess(instance().executeCommand(source, "crust player hermes edit nickname mars"));
    CrustPlugin.instance.revokePermission(playerUuid, "crust.player");
    assertFailure(instance().executeCommand(source, "crust player hermes edit nickname mars"));
  }

  @Test
  void sendMessageToTestAudience() {
    TestAudience audience = new TestAudience();

    Assertions.assertFalse(audience.hasSentMessage());
    audience.sendMessage(Component.text("this is a message"));
    Assertions.assertTrue(audience.hasSentMessage());
  }

  @Test
  void badParameterInput() {
    UUID playerUuid = UUID.randomUUID();
    CommandSource source = new CommandSource(CommandSource.Type.PLAYER, playerUuid, new TestAudience());

    // green is a valid color
    assertSuccess(instance().executeCommand(source, "crust register double green"));
    // purple is not a valid color
    assertFailure(instance().executeCommand(source, "crust register trouble purple"));
    // typo
    assertFailure(instance().executeCommand(source, "crust registre double green"));
    // 10 is a valid number
    assertSuccess(instance().executeCommand(source, "crust age 10"));
    // 9 and 3/4 is not a valid number (but is a valid train platform!)
    assertFailure(instance().executeCommand(source, "crust age 9&3/4"));
  }

  @Test
  void spacesInParameter() {
    UUID playerUuid = UUID.randomUUID();
    CommandSource source = new CommandSource(CommandSource.Type.PLAYER, playerUuid, new TestAudience());

    List<String> completions = instance().completeCommand(source, "crust register thing ");
    Assertions.assertTrue(completions.contains("\"canary yellow\""));
    // needs quotes
    assertFailure(instance().executeCommand(source, "crust register thing canary yellow"));
    assertSuccess(instance().executeCommand(source, "crust register thing \"canary yellow\""));

    // still want to allow completion with quotes even if it doesn't start that way
    completions = instance().completeCommand(source, "crust register thing ca");
    Assertions.assertTrue(completions.contains("\"canary yellow\""));
  }

  @Test
  void playerOnlyCommands() {
    CommandSource source = new CommandSource(CommandSource.Type.PLAYER, UUID.randomUUID(), new TestAudience());
    assertSuccess(instance().executeCommand(source, "crust age 10"));
    source = new CommandSource(CommandSource.Type.CONSOLE, null, new TestAudience());
    assertFailure(instance().executeCommand(source, "crust age 10"));
  }

}
