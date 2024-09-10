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
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.connector.CommandRoot;
import net.whimxiqal.mantle.common.phase.IdentifierParsePhase;
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
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * A command represented only in Mantle terms.
 * A Mantle Command is meant to be converted into a command understood by mod platforms.
 */
public class MantleCommand {

  private final CommandConnector connector;
  private final CommandRoot root;

  public MantleCommand(CommandConnector connector, CommandRoot commandRoot) {
    this.connector = connector;
    this.root = commandRoot;
  }

  /**
   * Get the connector with the information about the ANTLR generated files.
   *
   * @return the connector
   */
  public CommandConnector getConnector() {
    return connector;
  }

  /**
   * Process a command.
   *
   * @param source        the command source, the one executing the command
   * @param justArguments just the arguments of the command (without the base command)
   * @return the result of the command executing
   */
  public final CommandResult process(CommandSource source, String justArguments) {
    String arguments = root.baseCommand() + " " + justArguments;

    CharStream input = CharStreams.fromString(arguments);

    MantleErrorListener errorListener = new MantleErrorListener(connector, arguments);

    Lexer lexer = connector.lexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(errorListener);

    TokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = connector.parser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(errorListener);
    ParseTree parseTree = connector.baseContext(parser, root);

    if (errorListener.hasError()) {
      errorListener.sendErrorMessage(source);
      return CommandResult.failure();
    }

    IdentifierTrackerImpl tracker = new IdentifierTrackerImpl();
    CommandContext context = new CommandContextImpl(source, tracker);

    Optional<CommandResult> phaseResult = runPhases(source, parseTree, tracker, true);
    if (phaseResult.isPresent()) {
      return phaseResult.get();
    }

    ParseTreeVisitor<CommandResult> executor = connector.executor().provide(context);
    CommandResult result = executor.visit(parseTree);
    if (result == null) {
      return CommandResult.failure();
    }
    return result;
  }

  /**
   * Complete a command and give a set of suggestions that could complete the given arguments.
   *
   * @param source        the command source, the one executing the command
   * @param justArguments just the arguments of the command (without the base command)
   * @return the list of suggestions
   */
  public final synchronized List<String> complete(CommandSource source, String justArguments) {
    String arguments = root.baseCommand() + " " + justArguments;
    final boolean argumentsEndInWhitespace = Character.isWhitespace(arguments.charAt(arguments.length() - 1));
    String trimmedArgs = arguments.trim();
    CharStream input = CharStreams.fromString(trimmedArgs);

    Lexer lexer = connector.lexer(input);
    TokenStream tokens = new CommonTokenStream(lexer);
    Parser parser = connector.parser(tokens);

    lexer.removeErrorListeners();
    parser.removeErrorListeners();
    MantleErrorListener errorListener = new MantleErrorListener(connector, arguments);
    lexer.addErrorListener(errorListener);
    ParserRuleContext parseTree = connector.baseContext(parser, root);

    if (errorListener.hasError()) {
      return Collections.emptyList();
    }

    IdentifierTrackerImpl tracker = new IdentifierTrackerImpl();
    CommandContext context = new CommandContextImpl(source, tracker);

    Optional<CommandResult> result = runPhases(source, parseTree, tracker, false);
    if (result.isPresent()) {
      return Collections.emptyList();
    }
    return completionsFor(context, parser, parseTree, trimmedArgs, argumentsEndInWhitespace);
  }

  private List<String> completionsFor(CommandContext context, Parser parser, ParserRuleContext parseTree,
                                      String arguments, boolean argumentsEndInWhitespace) {
    final int identifierRule = connector.identifierInfo().identifierRule();
    CodeCompletionCore core = new CodeCompletionCore(parser,
        Collections.singleton(identifierRule),
        connector.identifierInfo().ignoredCompletionTokens());

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
          return permission == null || context.source().hasPermission(permission);
        })
        .map(t -> parser.getVocabulary().getLiteralName(t))
        .filter(s -> Objects.nonNull(s) && s.length() > 2)
        .map(s -> s.substring(1, s.length() - 1))  // get rid of the starting and ending quotes
        .forEach(possibleCompletions::add);

    // ** Identifiers **
    for (Map.Entry<Integer, List<Integer>> rule : collection.rules.entrySet()) {
      // TODO We could probably use the identifier parse phase in some way to get this information
      //  rather than this horrible "backtrack" garbage

      // the index of the rule we want to complete is the one after already completed ones.
      // Since core.collectCandidates does not keep track of how many completed identifiers there were,
      // we must backtrack through them, completing the command at previous points, and count how many times
      // we get a completed identifier, but with the same original rule call stack.
      int previousCompletedIdentifiers = 0;
      CodeCompletionCore.CandidatesCollection backtrackCollection = collection;
      String backtrackArguments = arguments;
      while (backtrackCollection.rulePositions.containsKey(identifierRule)) {
        // we have a completed identifier
        final int endingColumn = backtrackCollection.rulePositions.get(identifierRule).get(1);
        backtrackArguments = backtrackArguments.substring(0, endingColumn);
        if (backtrackArguments.isEmpty()) {
          break;
        }

        CharStream backtrackInput = CharStreams.fromString(backtrackArguments);

        Lexer backtrackLexer = connector.lexer(backtrackInput);
        TokenStream backtrackTokens = new CommonTokenStream(backtrackLexer);
        Parser backtrackParser = connector.parser(backtrackTokens);

        backtrackLexer.removeErrorListeners();
        backtrackParser.removeErrorListeners();
        MantleErrorListener errorListener = new MantleErrorListener(connector, backtrackArguments);
        backtrackLexer.addErrorListener(errorListener);

        ParserRuleContext backtrackParseTree = connector.baseContext(backtrackParser, root);

        if (errorListener.hasError()) {
          return Collections.emptyList();
        }

        CodeCompletionCore backtrackCore = new CodeCompletionCore(backtrackParser,
            Collections.singleton(identifierRule),
            connector.identifierInfo().ignoredCompletionTokens());

        CaretTokenIndexResult backtrackCaretTokenIndexResult = getCaretTokenIndex(backtrackParseTree,
            backtrackArguments.length());
        if (backtrackCaretTokenIndexResult.index < 0) {
          break;
        }
        backtrackCollection = backtrackCore.collectCandidates(backtrackCaretTokenIndexResult.index, backtrackParseTree);

        List<Integer> backtrackCallStack = backtrackCollection.rules.get(rule.getKey());  // could be null
        if (!rule.getValue().equals(backtrackCallStack)) {
          // check if we are not in the same rule anymore, so none of the other previous identifiers are relevant,
          // including the one we just found
          break;
        }
        previousCompletedIdentifiers++;
      }
      int completableIndex = previousCompletedIdentifiers;  // the index of the last completed identifier + 1
      if (rule.getValue().isEmpty()) {
        continue;
      }
      int caller = rule.getValue().get(rule.getValue().size() - 1);  // caller is the last one in the stack
      possibleCompletions.addAll(connector.identifierInfo().completeIdentifier(context,
          caller,
          completableIndex));
    }
    return possibleCompletions.stream()
        .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(currentText.toLowerCase(Locale.ENGLISH)))
        .map(s -> s.contains(" ") ? "\"" + s + "\"" : s)
        .collect(Collectors.toList());
  }

  private Optional<CommandResult> runPhases(CommandSource source, ParseTree tree,
                                            IdentifierTrackerImpl tracker, boolean validate) {
    ParsePhase[] phases = {
        new PermissionParsePhase(connector, validate),
        new PlayerOnlyParsePhase(connector),
        new IdentifierParsePhase(connector, tracker, validate),
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
