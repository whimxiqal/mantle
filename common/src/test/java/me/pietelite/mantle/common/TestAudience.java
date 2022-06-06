package me.pietelite.mantle.common;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class TestAudience implements Audience {

  @Override
  public void sendMessage(final @NotNull Identity source,
                          final @NotNull Component message,
                          final @NotNull MessageType type) {
    System.out.println(message);
  }
}
