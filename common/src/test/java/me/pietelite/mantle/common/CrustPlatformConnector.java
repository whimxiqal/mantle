package me.pietelite.mantle.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.jetbrains.annotations.Nullable;

public class CrustPlatformConnector implements CommandConnector {

  private final Map<Integer, String> rulePermissions = new HashMap<>();

  {
    rulePermissions.put(CrustParser.RULE_register, "crust.register");
    rulePermissions.put(CrustParser.RULE_unregister, "crust.unregister");
    rulePermissions.put(CrustParser.RULE_player, "crust.player");
    rulePermissions.put(CrustParser.RULE_playerEdit, "crust.player.edit");
  }

  @Override
  public Lexer lexer(CharStream input) {
    return new CrustLexer(input);
  }

  @Override
  public Parser parser(TokenStream tokenStream) {
    return new CrustParser(tokenStream);
  }

  @Override
  public ParseTree parserToParseTree(Parser parser) {
    return ((CrustParser) parser).crust();
  }

  @Override
  public ParseTreeVisitor<Boolean> executor() {
    return new CrustCommandExecutor();
  }

  @Override
  public @Nullable ParseTreeVisitor<Component> descriptionHandler() {
    return null;
  }

  @Override
  public List<String> aliases() {
    return Collections.singletonList("crust");
  }

  @Override
  public Map<Integer, String> rulePermissions() {
    return rulePermissions;
  }

  public void registerTo(CrustPlugin plugin) {
    plugin.registerCommand("crust", new MantleCommand(this));
  }
}
