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

package net.whimxiqal.mantle.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import net.whimxiqal.mantle.common.connector.IdentifierInfo;
import net.whimxiqal.mantle.common.parameter.Parameter;
import net.whimxiqal.mantle.common.parameter.Parameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.whimxiqal.mantle.common.CrustParser.*;

public class MantleTest {

  private static final List<String> COLORS = new LinkedList<>();
  private static final List<String> COLOR_COMPLETIONS = new LinkedList<>();

  static {
    COLORS.add("blue");
    COLOR_COMPLETIONS.add("blue");
    COLORS.add("green");
    COLOR_COMPLETIONS.add("green");
    COLORS.add("red");
    COLOR_COMPLETIONS.add("red");
    COLORS.add("canary yellow");
    COLOR_COMPLETIONS.add("\"canary yellow\"");
  }

  @BeforeAll
  static void setUpAll() {
    Mantle.setProxy(new CrustPlatformProxy());
    CrustPlugin.instance = new CrustPlugin();
    CrustPlugin.instance.registerCommand(CommandConnector.builder()
        .addRoot(CommandRoot.builder("crust").build())
        .addRoot(CommandRoot.builder("core").build())
        .lexer(CrustLexer.class)
        .parser(CrustParser.class)
        .executor(context -> new CrustBaseVisitor<CommandResult>() {
          @Override
          public CommandResult visitRegister(CrustParser.RegisterContext ctx) {
            String playerName1 = context.identifiers().get(0);
            String playerName2 = ctx.name.getText();
            Assertions.assertEquals(playerName1, playerName2);
            boolean added = CrustPlugin.instance.players.add(playerName1);
            if (added) {
              context.source().audience().sendMessage(Component.text("The player was added"));
              return CommandResult.success();
            } else {
              context.source().audience().sendMessage(Component.text("A player already exists with that name"));
              return CommandResult.failure();
            }
          }

          @Override
          public CommandResult visitUnregister(CrustParser.UnregisterContext ctx) {
            String playerName1 = context.identifiers().get(Parameters.PLAYER, 0);
            String playerName2 = ctx.name.getText();
            Assertions.assertEquals(playerName1, playerName2);
            boolean removed = CrustPlugin.instance.players.remove(playerName1);
            if (removed) {
              context.source().audience().sendMessage(Component.text("The player was removed"));
              return CommandResult.success();
            } else {
              context.source().audience().sendMessage(Component.text("The player could not be removed"));
              return CommandResult.failure();
            }
          }

          @Override
          public CommandResult visitPlayerEditNickname(CrustParser.PlayerEditNicknameContext ctx) {
            return CommandResult.success();
          }

          @Override
          public CommandResult visitAge(CrustParser.AgeContext ctx) {
            int age = Integer.parseInt(context.identifiers().get(Parameters.INTEGER, 0));
            context.source().audience().sendMessage(Component.text("You are " + age + " years old!"));
            return CommandResult.success();
          }
        })
        .addPermission(RULE_register, "crust.register")
        .addPermission(RULE_unregister, "crust.unregister")
        .addPermission(RULE_player, "crust.player")
        .addPermission(RULE_playerEdit, "crust.player.edit")
        .identifierInfo(IdentifierInfo.builder(RULE_identifier, IdentifierContext.class)
            .addParameter(Parameter.builder("color")
                .options(ctx -> COLORS)
                .validator(COLORS::contains)
                .build())
            .standardExtractor(IdentifierContext::ident)
            .registerCompletion(RULE_player, 0, Parameters.PLAYER)
            .registerCompletion(RULE_register, 1, "color")
            .registerCompletion(RULE_register, 3, Parameters.PLAYER)
            .registerCompletion(RULE_unregister, 0, Parameters.PLAYER)
            .registerCompletion(RULE_core, 0, "color")
            .registerCompletion(RULE_playerEditColor, 0, "color")
            .registerCompletion(RULE_age, 0, Parameters.INTEGER)
            .addIgnoredCompletionToken(CrustLexer.SINGLE_QUOTE)
            .addIgnoredCompletionToken(CrustLexer.DOUBLE_QUOTE)
            .build())
        .playerOnlyCommands(RULE_age)
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

  void assertSuccess(CommandResult result) {
    Assertions.assertEquals(CommandResult.Type.SUCCESS, result.type());
  }

  void assertFailure(CommandResult result) {
    Assertions.assertEquals(CommandResult.Type.FAILURE, result.type());
  }

  @Test
  void completeCommand() {
    CrustPlugin.instance.players.add("PietElite");
    CrustPlugin.instance.players.add("belkar1");

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
    Assertions.assertEquals(CrustPlugin.instance.players.size(), completions.size());
    for (String player : CrustPlugin.instance.players) {
      Assertions.assertTrue(completions.contains(player));
    }

    completions = instance().completeCommand(source, "crust player p");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("PietElite"));

    completions = instance().completeCommand(source, "crust player P");
    Assertions.assertEquals(1, completions.size());
    Assertions.assertTrue(completions.contains("PietElite"));

    completions = instance().completeCommand(source, "crust player golem ");
    Assertions.assertEquals(2, completions.size(), completions.toString());
    Assertions.assertTrue(completions.contains("info"));
    Assertions.assertTrue(completions.contains("edit"));

    completions = instance().completeCommand(source, "crust z");
    Assertions.assertEquals(0, completions.size());

    completions = instance().completeCommand(source, "crust register tornado ");
    Assertions.assertEquals(COLORS.size(), completions.size());
    for (String color : COLOR_COMPLETIONS) {
      Assertions.assertTrue(completions.contains(color));
    }

    completions = instance().completeCommand(source, "crust register a b c ");
    Assertions.assertEquals(CrustPlugin.instance.players.size(), completions.size());
    for (String player : CrustPlugin.instance.players) {
      Assertions.assertTrue(completions.contains(player));
    }

    completions = instance().completeCommand(source, "crust player name edit color ");
    Assertions.assertEquals(COLORS.size(), completions.size());
    for (String color : COLOR_COMPLETIONS) {
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
    Assertions.assertEquals(COLORS.size(), completions.size());
    for (String color : COLOR_COMPLETIONS) {
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
    Set<String> players = CrustPlugin.instance.players;
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
    Set<String> hosts = CrustPlugin.instance.players;
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
