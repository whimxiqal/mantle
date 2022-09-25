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

import net.kyori.adventure.text.Component;

public class CrustCommandExecutor extends CrustBaseVisitor<Boolean> {

  @Override
  public Boolean visitRegister(CrustParser.RegisterContext ctx) {
    CommandSource source = Mantle.session().getSource();
    String playerName = ctx.user.getText();
    boolean added = CrustPlugin.instance.players.add(playerName);
    if (added) {
      source.getAudience().sendMessage(Component.text("The player was added"));
      return true;
    } else {
      source.getAudience().sendMessage(Component.text("A player already exists with that name"));
      return false;
    }
  }

  @Override
  public Boolean visitUnregister(CrustParser.UnregisterContext ctx) {
    CommandSource source = Mantle.session().getSource();
    boolean removed = CrustPlugin.instance.players.remove(ctx.identifier().getText());
    if (removed) {
      source.getAudience().sendMessage(Component.text("The player was removed"));
      return true;
    } else {
      source.getAudience().sendMessage(Component.text("The player could not be removed"));
      return false;
    }
  }

  @Override
  public Boolean visitPlayerEditNickname(CrustParser.PlayerEditNicknameContext ctx) {
    return true;
  }
}
