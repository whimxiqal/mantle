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

package net.whimxiqal.mantle.common.phase;

import java.util.Optional;
import net.whimxiqal.mantle.common.CommandResult;
import net.whimxiqal.mantle.common.CommandSource;
import net.whimxiqal.mantle.common.IdentifierTrackerImpl;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import net.whimxiqal.mantle.common.parameter.Parameter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * A unit of parsing to be done before the main user-defined execution processes.
 */
public class IdentifierParsePhase implements ParsePhase {
  private final CommandConnector connector;
  private final IdentifierListener identifierListener;

  public IdentifierParsePhase(CommandConnector connector, IdentifierTrackerImpl tracker, boolean validate) {
    this.connector = connector;
    this.identifierListener = new IdentifierListener(connector.identifierInfo(), tracker, validate);
  }

  @Override
  public Optional<CommandResult> walk(CommandSource source, ParseTree parseTree) {
    ParseTreeWalker walker = new ParseTreeWalker();
    if (connector.identifierInfo() == null) {
      return Optional.empty();
    }
    walker.walk(identifierListener, parseTree);
    Parameter invalid = identifierListener.getInvalid();
    if (invalid != null) {
      source.audience().sendMessage(invalid.invalidMessage());
      return Optional.of(CommandResult.failure());
    }
    return Optional.empty();
  }

}
