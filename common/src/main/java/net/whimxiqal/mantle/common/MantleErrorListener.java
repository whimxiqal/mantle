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

import net.kyori.adventure.text.Component;
import net.whimxiqal.mantle.common.connector.CommandConnector;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

class MantleErrorListener extends BaseErrorListener {

  private final CommandConnector connector;
  private final String command;
  private Component errorMessage;

  MantleErrorListener(CommandConnector connector, String command) {
    this.connector = connector;
    this.command = command;
  }

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                          int charPositionInLine, String msg, RecognitionException e) {
    this.errorMessage = connector.syntaxError(command.substring(charPositionInLine));
  }

  public boolean hasError() {
    return errorMessage != null;
  }

  public Component errorMessage() {
    return errorMessage;
  }

  public void sendErrorMessage(CommandSource source) {
    if (errorMessage != null) {
      source.audience().sendMessage(errorMessage);
    }
  }
}
