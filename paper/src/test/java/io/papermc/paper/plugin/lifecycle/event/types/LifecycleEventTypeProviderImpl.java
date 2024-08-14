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

package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.MonitorLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
@DefaultQualifier(NonNull.class)
public final class LifecycleEventTypeProviderImpl implements LifecycleEventTypeProvider {

  public static LifecycleEventTypeProviderImpl instance() {
    return (LifecycleEventTypeProviderImpl) LifecycleEventTypeProvider.PROVIDER;
  }

  @Override
  public <O extends LifecycleEventOwner, E extends LifecycleEvent> LifecycleEventType.Monitorable<O, E> monitor(final String name, final Class<? extends O> ownerType) {
    return new LifecycleEventType.Monitorable<O, E>() {
      @Override
      public @NotNull String name() {
        return "monitor";
      }

      @Override
      public @NotNull MonitorLifecycleEventHandlerConfiguration<O> newHandler(@NotNull LifecycleEventHandler<? super E> lifecycleEventHandler) {
        return new MonitorLifecycleEventHandlerConfiguration<O>() {
          @Override
          public MonitorLifecycleEventHandlerConfiguration<O> monitor() {
            return this;
          }
        };
      }
    };
  }

  @Override
    public <O extends LifecycleEventOwner, E extends LifecycleEvent> LifecycleEventType.Prioritizable<O, E> prioritized(final String name, final Class<? extends O> ownerType) {
//            return LifecycleEventRunner.INSTANCE.addEventType(new PrioritizableLifecycleEventType.Simple<>(name, ownerType));
    return new LifecycleEventType.Prioritizable<O, E>() {
      @Override
      public @NotNull String name() {
        return "prioritized";
      }

      @Override
      public @NotNull PrioritizedLifecycleEventHandlerConfiguration<O> newHandler(@NotNull LifecycleEventHandler<? super E> lifecycleEventHandler) {
        return new PrioritizedLifecycleEventHandlerConfiguration<O>() {
          @Override
          public PrioritizedLifecycleEventHandlerConfiguration<O> priority(int i) {
            return this;
          }

          @Override
          public PrioritizedLifecycleEventHandlerConfiguration<O> monitor() {
            return this;
          }
        };
      }
    };
  }
}
