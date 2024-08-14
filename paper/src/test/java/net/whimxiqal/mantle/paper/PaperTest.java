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

package net.whimxiqal.mantle.paper;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.whimxiqal.mantle.common.CommandRegistrar;
import net.whimxiqal.mantle.crust.CrustConnector;
import org.bukkit.command.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaperTest {

//  @Disabled("Disabled until MockBucket implements")
  @Test
  void testPaper() {
    ServerMock server = MockBukkit.mock();
    MockPlugin plugin = MockBukkit.createMockPlugin();

    CommandRegistrar registrar = PaperRegistrarProvider.get(plugin);
    registrar.register(CrustConnector.generate(PaperCrustVisitor::new));

    PlayerMock player = server.addPlayer();

    Command command = plugin.getCommand("crust");
    Assertions.assertNotNull(command);

    boolean res = plugin.onCommand(player, command, "crust", new String[]{});
    Assertions.assertTrue(res);

    MockBukkit.unmock();
  }

}
