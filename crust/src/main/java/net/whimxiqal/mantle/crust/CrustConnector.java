package net.whimxiqal.mantle.crust;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.CommandExecutor;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import net.whimxiqal.mantle.common.connector.IdentifierInfo;
import net.whimxiqal.mantle.common.parameter.Parameter;
import net.whimxiqal.mantle.common.parameter.Parameters;

import static net.whimxiqal.mantle.crust.CrustParser.*;
import static net.whimxiqal.mantle.crust.CrustParser.RULE_age;

public class CrustConnector {

  public static final String[] COLORS = {"blue", "green", "red", "canary yellow"};
  public static final String[] COLOR_COMPLETIONS = {"blue", "green", "red", "\"canary yellow\""};

  static public CommandConnector generate(CommandExecutor executor) {
    return CommandConnector.builder()
        .addRoot(CommandRoot.builder("crust")
            .description(Component.text("Crust Command"))
            .addAlias("cr")
            .build())
        .addRoot(CommandRoot.builder("core")
            .description(Component.text("Core Command"))
            .addAlias("co")
            .build())
        .lexer(CrustLexer.class)
        .parser(CrustParser.class)
        .executor(executor)
        .addPermission(RULE_register, "crust.register")
        .addPermission(RULE_unregister, "crust.unregister")
        .addPermission(RULE_player, "crust.player")
        .addPermission(RULE_playerEdit, "crust.player.edit")
        .identifierInfo(IdentifierInfo.builder(RULE_identifier, IdentifierContext.class)
            .addParameter(Parameter.builder("color")
                .options(ctx -> Arrays.asList(COLORS))
                .validator(val -> Arrays.asList(COLORS).contains(val))
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
        .setSyntaxErrorFunction(err -> Component.text("Oops!! " + err))
        .build();
  }

}
