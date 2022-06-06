package me.pietelite.mantle.common;

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

public interface CommandConnector {

  Lexer lexer(CharStream input);

  Parser parser(TokenStream tokenStream);

  ParseTree parserToParseTree(Parser parser);

  ParseTreeVisitor<Boolean> executor();

  @Nullable
  default ParseTreeVisitor<Component> descriptionHandler() {
    return null;
  }

  List<String> aliases();

  /**
   * A map of grammar rule indexes to permissions.
   * If a rule's index does not appear in this map, then the rule
   * is considered fully permissible.
   *
   * @return a map of rule indexes to permissions
   */
  Map<Integer, String> rulePermissions();

}
