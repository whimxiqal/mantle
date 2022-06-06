package me.pietelite.mantle.common;

import com.vmware.antlr4c3.CodeCompletionCore;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MantleCommand {

  private final CommandConnector connector;

  public MantleCommand(CommandConnector connector) {
    this.connector = connector;
  }

  public CommandConnector getConnector() {
    return connector;
  }

  public final boolean process(CommandSource source, String arguments) {
    Mantle.setSession(new CommandSession(source));
    CharStream input = CharStreams.fromString(arguments);

    Lexer lexer = connector.lexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = connector.parser(tokens);

    parser.removeErrorListeners();
    MantleErrorListener errorListener = new MantleErrorListener();
    parser.addErrorListener(errorListener);
    ParseTree parseTree = connector.parserToParseTree(parser);

    if (errorListener.hasError()) {
      return false;
    }

    if (isRestricted(parseTree)) {
      Mantle.session().getSource()
          .getAudience()
          .sendMessage(Component.text("You don't have permissions to do that").color(NamedTextColor.RED));
      return false;
    }

    ParseTreeVisitor<Boolean> executor = connector.executor();
    Boolean result = executor.visit(parseTree);
    if (result == null) {
      return false;
    }
    return result;
  }

  public final List<String> complete(CommandSource source, String arguments) {
    Mantle.setSession(new CommandSession(source));
    boolean argumentsEndInWhitespace = !arguments.isEmpty()
        && Character.isWhitespace(arguments.charAt(arguments.length() - 1));
    String trimmedArgs = arguments.trim();
    CharStream input = CharStreams.fromString(trimmedArgs);

    Lexer lexer = connector.lexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = connector.parser(tokens);

    parser.removeErrorListeners();
    MantleErrorListener errorListener = new MantleErrorListener();
    parser.addErrorListener(errorListener);
    ParseTree parseTree = connector.parserToParseTree(parser);

    if (isRestricted(parseTree)) {
      // This command is not even allowed by this person
      return Collections.emptyList();
    }

    CodeCompletionCore core = new CodeCompletionCore(parser, null, null);

    CaretTokenIndexResult caretTokenIndexResult;
    if (arguments.isEmpty()) {
      caretTokenIndexResult = new CaretTokenIndexResult(0, "");
    } else {
      caretTokenIndexResult = getCaretTokenIndex(parseTree, trimmedArgs.length());
      if (caretTokenIndexResult.index < 0) {
        return Collections.emptyList();
      }
      if (argumentsEndInWhitespace) {
        // Move ahead if we have a space at the end of the command; we want to complete the next thing
        caretTokenIndexResult = new CaretTokenIndexResult(caretTokenIndexResult.index + 1, "");
      }
    }
    CodeCompletionCore.CandidatesCollection collection = core.collectCandidates(caretTokenIndexResult.index, null);
    String currentText = caretTokenIndexResult.text;
    return collection.tokens.keySet().stream()
        .filter(t -> {
          // This token is not allowed by this person
          String permission = connector.rulePermissions().get(t);
          return permission == null || Mantle.sourceHasPermission(permission);
        })
        .map(t -> parser.getVocabulary().getLiteralName(t))
        .filter(s -> Objects.nonNull(s) && s.length() > 2)
        .map(s -> s.substring(1, s.length() - 1))  // get rid of the starting and ending quotes
        .filter(s -> s.startsWith(currentText))
        .collect(Collectors.toList());
  }

  private CaretTokenIndexResult getCaretTokenIndex(ParseTree parseTree, int column) {
    if (parseTree instanceof TerminalNode) {
      return getCaretTokenIndexTerminalNode((TerminalNode) parseTree, column);
    } else {
      return getCaretTokenIndexChildNode(parseTree, column);
    }
  }

  private CaretTokenIndexResult getCaretTokenIndexTerminalNode(TerminalNode parseTree, int column) {
    assert (parseTree.getSymbol().getLine() == 1);
    int start = parseTree.getSymbol().getCharPositionInLine();
    int end = start + parseTree.getSymbol().getText().length();
    if (start <= column && end >= column) {
      return new CaretTokenIndexResult(parseTree.getSymbol().getTokenIndex(), parseTree.getSymbol().getText());
    } else {
      return CaretTokenIndexResult.none();
    }
  }

  private CaretTokenIndexResult getCaretTokenIndexChildNode(ParseTree parseTree, int column) {
    for (int i = 0; i < parseTree.getChildCount(); i++) {
      CaretTokenIndexResult result = getCaretTokenIndex(parseTree.getChild(i), column);
      if (result.index >= 0) {
        return result;
      }
    }
    return CaretTokenIndexResult.none();
  }

  private boolean isRestricted(ParseTree parseTree) {
    PermissionListener permissionListener = new PermissionListener(connector.rulePermissions());
    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(permissionListener, parseTree);
    return !permissionListener.isAllowed();
  }

  private static class CaretTokenIndexResult {
    int index;
    String text;

    CaretTokenIndexResult(int index, String text) {
      this.index = index;
      this.text = text;
    }

    static CaretTokenIndexResult none() {
      return new CaretTokenIndexResult(-1, "");
    }
  }

}
