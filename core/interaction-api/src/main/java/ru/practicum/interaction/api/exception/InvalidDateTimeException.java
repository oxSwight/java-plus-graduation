package ru.practicum.interaction.api.exception;

public class InvalidDateTimeException extends RuntimeException {
  public InvalidDateTimeException(String message) {
    super(message);
  }
}
