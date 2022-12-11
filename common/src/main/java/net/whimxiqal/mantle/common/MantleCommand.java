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

import com.vmware.antlr4c3.CodeCompletionCore;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import net.whimxiqal.mantle.common.connector.HelpCommandInfo;
import net.whimxiqal.mantle.common.phase.ParsePhase;
import net.whimxiqal.mantle.common.phase.PermissionParsePhase;
import net.whimxiqal.mantle.common.phase.PlayerOnlyParsePhase;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * A command represented only in Mantle terms.
 * A Mantle Command is meant to be converted into a command understood by mod platforms.
 */
public class MantleCommand {

  private static final String[] HELP_COMMAND_ARGS = {"?", "help"};

  private final CommandConnector connector;
  private final CommandRoot root;

  public MantleCommand(CommandConnector connector, CommandRoot commandRoot) {
    this.connector = connector;
    this.root = commandRoot;
  }

  public CommandConnector getConnector() {
    return connector;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public final CommandResult process(CommandSource source, String justArguments) {
    String arguments = root.baseCommand() + " " + justArguments;
    if (executeHelpCommand(source, arguments)) {
      return CommandResult.success();
    }

    CharStream input = CharStreams.fromString(arguments);

    Lexer lexer = connector.lexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = connector.parser(tokens);

    parser.removeErrorListeners();
    MantleErrorListener errorListener = new MantleErrorListener();
    parser.addErrorListener(errorListener);
    ParseTree parseTree = connector.baseContext(parser, root);

    if (errorListener.hasError()) {
      Optional<String> message = errorListener.message();
      if (message.isPresent() && connector.useDefaultParseError()) {
        source.audience().sendMessage(Component.text(message.get()).color(NamedTextColor.DARK_RED));
      }
      return CommandResult.failure();
    }

    Optional<CommandResult> phaseResult = runPhases(source, parseTree);
    if (phaseResult.isPresent()) {
      return phaseResult.get();
    }

    ParseTreeVisitor<CommandResult> executor = connector.executor().provide(source);
    CommandResult result = executor.visit(parseTree);
    if (result == null) {
      return CommandResult.failure();
    }
    return result;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public final synchronized List<String> complete(CommandSource source, String justArguments) {
    String arguments = root.baseCommand() + " " + justArguments;
    final boolean argumentsEndInWhitespace = Character.isWhitespace(arguments.charAt(arguments.length() - 1));
    String trimmedArgs = arguments.trim();
    CharStream input = CharStreams.fromString(trimmedArgs);

    Lexer lexer = connector.lexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = connector.parser(tokens);

    parser.removeErrorListeners();
    MantleErrorListener errorListener = new MantleErrorListener();
    parser.addErrorListener(errorListener);
    ParserRuleContext parseTree = connector.baseContext(parser, root);

    Optional<CommandResult> result = runPhases(source, parseTree);
    if (result.isPresent()) {
      return Collections.emptyList();
    }

    return completionsFor(source, parser, parseTree, trimmedArgs, argumentsEndInWhitespace);
  }

  private List<String> completionsFor(CommandSource source, Parser parser, ParserRuleContext parseTree,
                                      String arguments, boolean argumentsEndInWhitespace) {
    CodeCompletionCore core = new CodeCompletionCore(parser,
        connector.completionInfo().completableRules(),
        connector.completionInfo().ignoredCompletionTokens());

    CaretTokenIndexResult caretTokenIndexResult;
    if (arguments.isEmpty()) {
      caretTokenIndexResult = new CaretTokenIndexResult(0, "");
    } else {
      caretTokenIndexResult = getCaretTokenIndex(parseTree, arguments.length());
      if (caretTokenIndexResult.index < 0) {
        return Collections.emptyList();
      }
      if (argumentsEndInWhitespace) {
        // Move ahead if we have a space at the end of the command; we want to complete the next thing
        caretTokenIndexResult = new CaretTokenIndexResult(caretTokenIndexResult.index + 1, "");
      }
    }
    CodeCompletionCore.CandidatesCollection collection = core.collectCandidates(caretTokenIndexResult.index, parseTree);
    String currentText = caretTokenIndexResult.text;

    List<String> possibleCompletions = new LinkedList<>();

    // Tokens
    collection.tokens.keySet().stream()
        .filter(t -> {
          // This token is not allowed by this person
          String permission = connector.rulePermissions().get(t);
          return permission == null || source.hasPermission(permission);
        })
        .map(t -> parser.getVocabulary().getLiteralName(t))
        .filter(s -> Objects.nonNull(s) && s.length() > 2)
        .map(s -> s.substring(1, s.length() - 1))  // get rid of the starting and ending quotes
        .forEach(possibleCompletions::add);

    // Rules
    // the index of the rule we want to complete is the one after already completed ones
    int completableIndex = collection.rulePositions.size();
    for (Map.Entry<Integer, List<Integer>> rule : collection.rules.entrySet()) {
      if (rule.getValue().isEmpty()) {
        continue;
      }
      int caller = rule.getValue().get(rule.getValue().size() - 1);  // caller is the last one in the stack
      possibleCompletions.addAll(connector.completionInfo().completionsFor(source,
          caller,
          rule.getKey(),
          completableIndex));
    }
    return possibleCompletions.stream()
        .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(currentText.toLowerCase(Locale.ENGLISH)))
        .collect(Collectors.toList());
  }

  private Optional<CommandResult> runPhases(CommandSource source, ParseTree tree) {
    ParsePhase[] phases = {
        new PermissionParsePhase(connector),
        new PlayerOnlyParsePhase(connector),
    };
    for (ParsePhase phase : phases) {
      Optional<CommandResult> result = phase.walk(source, tree);
      if (result.isPresent()) {
        return result;
      }
    }
    return Optional.empty();
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

  private boolean executeHelpCommand(CommandSource source, String argumentsWithHelp) {
    HelpCommandInfo helpCommandInfo = connector.helpCommandInfo();
    if (helpCommandInfo == null) {
      return false;
    }
    String arguments = "";
    boolean isHelpCommand = false;
    for (String arg : HELP_COMMAND_ARGS) {
      int idx = argumentsWithHelp.length() - arg.length() - 1;
      if (argumentsWithHelp.length() > arg.length()
          && argumentsWithHelp.substring(idx).equals(" " + arg)) {
        arguments = argumentsWithHelp.substring(0, idx).trim();
        isHelpCommand = true;
        break;
      }
    }
    if (!isHelpCommand) {
      return false;
    }

    CharStream input = CharStreams.fromString(arguments);

    Lexer lexer = connector.lexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = connector.parser(tokens);

    parser.removeErrorListeners();
    MantleErrorListener errorListener = new MantleErrorListener();
    parser.addErrorListener(errorListener);

    ParserRuleContext ruleContext = connector.baseContext(parser, root);
    DescriptionListener descriptionListener = new DescriptionListener(helpCommandInfo);
    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(descriptionListener, ruleContext);

    Optional<Component> description = descriptionListener.description();
    if (description.isPresent()) {
      source.audience().sendMessage(helpCommandInfo.header());
      source.audience().sendMessage(Component.text("Description: ").append(description.get()));
      List<String> next = completionsFor(source, parser, ruleContext, arguments, true);
      for (String n : next) {
        source.audience().sendMessage(Component.text("> ").append(Component.text(n)));
      }
    } else {
      source.audience().sendMessage(Component.text("No help command found"));
    }
    return true;
  }

  private static class CaretTokenIndexResult {
    private final int index;
    private final String text;

    CaretTokenIndexResult(int index, String text) {
      this.index = index;
      this.text = text;
    }

    static CaretTokenIndexResult none() {
      return new CaretTokenIndexResult(-1, "");
    }
  }

}
