package me.pietelite.mantle.common;

import java.util.Optional;
import net.kyori.adventure.text.Component;

/**
 * A result of a command.
 */
public interface CommandResult {

  /**
   * Generate a builder for a command result.
   *
   * @return the builder
   */
  static CommandResultBuilder builder() {
    return new CommandResultBuilder();
  }

  /**
   * Generate a basic success command result.
   *
   * @return a success result
   */
  static CommandResult success() {
    return builder().type(Type.SUCCESS).build();
  }

  /**
   * Generate a basic failure command result.
   *
   * @return a basic failure
   */
  static CommandResult failure() {
    return builder().type(Type.FAILURE).build();
  }

  /**
   * Generate a basic failure command result with a message.
   * <b>The message is currently unused.</b>
   *
   * @param message a basic failure with a message
   * @return a failure
   */
  static CommandResult failure(Component message) {
    return builder().type(Type.FAILURE).message(message).build();
  }

  /**
   * The type of result.
   *
   * @return the result's type
   */
  Type type();

  /**
   * An optional message associated with the result.
   * <b>Currently Unused</b>
   *
   * @return the message
   */
  Optional<Component> message();

  /**
   * The type of command result.
   */
  enum Type {
    EMPTY,
    SUCCESS,
    FAILURE
  }

}
