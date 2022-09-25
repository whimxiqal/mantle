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

package me.pietelite.mantle.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.pietelite.mantle.common.connector.CommandConnector;
import me.pietelite.mantle.common.connector.CompletionInfo;
import me.pietelite.mantle.common.connector.HelpCommandInfo;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static me.pietelite.mantle.common.CrustParser.RULE_crust;
import static me.pietelite.mantle.common.CrustParser.RULE_identifier;
import static me.pietelite.mantle.common.CrustParser.RULE_player;
import static me.pietelite.mantle.common.CrustParser.RULE_playerEdit;
import static me.pietelite.mantle.common.CrustParser.RULE_playerEditNickname;
import static me.pietelite.mantle.common.CrustParser.RULE_playerInfo;
import static me.pietelite.mantle.common.CrustParser.RULE_register;
import static me.pietelite.mantle.common.CrustParser.RULE_unregister;

public class MantleTest {

  private static final List<String> COLORS = new LinkedList<>();

  {
    COLORS.add("blue");
    COLORS.add("green");
    COLORS.add("red");
  }

  @BeforeAll
  static void setUpAll() {
    Mantle.setProxy(new CrustPlatformProxy());
    CrustPlugin.instance = new CrustPlugin();
    CrustPlugin.instance.registerCommand("crust",
        CommandConnector.builder().setBaseCommand("crust")
            .setLexerClass(CrustLexer.class)
            .setParserClass(CrustParser.class)
            .setExecutionHandler(CrustCommandExecutor::new)
            .setHelpCommandInfo(HelpCommandInfo.builder()
                .addDescription(RULE_crust, Component.text("Basic crust command"))
                .addDescription(RULE_register, Component.text("Register a player for a thing"))
                .addDescription(RULE_unregister, Component.text("Unregister a registered player"))
                .addDescription(RULE_player, Component.text("Edit or see information about a player"))
                .addDescription(RULE_playerInfo, Component.text("Lookup information about a player"))
                .addDescription(RULE_playerEdit, Component.text("Edit information about a player"))
                .addDescription(RULE_playerEditNickname, Component.text("Edit the nickname of a player"))
                .addIgnored(RULE_identifier)
                .build())
            .addPermission(RULE_register, "crust.register")
            .addPermission(RULE_unregister, "crust.unregister")
            .addPermission(RULE_player, "crust.player")
            .addPermission(RULE_playerEdit, "crust.player.edit")
            .setCompletionInfo(CompletionInfo.builder()
                .addParameter("color", COLORS)
                .registerCompletion(RULE_player, RULE_identifier, 0, "player")
                .registerCompletion(RULE_register, RULE_identifier, 0, "player")
                .registerCompletion(RULE_register, RULE_identifier, 1, "color")
                .addIgnoredCompletionToken(CrustLexer.SINGLE_QUOTE)
                .addIgnoredCompletionToken(CrustLexer.DOUBLE_QUOTE)
                .build())
            .build());
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

    completions = instance().completeCommand(source, "crust player ");
    Assertions.assertEquals(CrustPlatformProxy.PLAYERS.size(), completions.size());
    for (String player : CrustPlatformProxy.PLAYERS) {
      Assertions.assertTrue(completions.contains(player));
    }

    completions = instance().completeCommand(source, "crust player p");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("PietElite"));

    completions = instance().completeCommand(source, "crust player P");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("PietElite"));

    completions = instance().completeCommand(source, "crust player golem ");
    Assertions.assertEquals(2, completions.size());
    Assertions.assertTrue(completions.contains("info"));
    Assertions.assertTrue(completions.contains("edit"));

    completions = instance().completeCommand(source, "crust z");
    Assertions.assertEquals(0, completions.size());

    completions = instance().completeCommand(source, "crust register tornado ");
    Assertions.assertEquals(COLORS.size(), completions.size());
    for (String color : COLORS) {
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

  @Test
  void helpCommand() {
    UUID playerUuid = UUID.randomUUID();
    TestAudience audience = new TestAudience();
    CommandSource source = new CommandSource(CommandSource.Type.PLAYER, playerUuid, audience);
    Assertions.assertTrue(instance().executeCommand(source, "crust player ?"));
    Assertions.assertTrue(audience.hasSentMessage());
  }

  @Test
  void sendMessageToTestAudience() {
    TestAudience audience = new TestAudience();

    Assertions.assertFalse(audience.hasSentMessage());
    audience.sendMessage(Component.text("this is a message"));
    Assertions.assertTrue(audience.hasSentMessage());
  }

}
