package com.example.task.exception;

public class CreateForbiddenException extends RuntimeException {
  public CreateForbiddenException(String message) {
    super(message);
  }

  public CreateForbiddenException(String message, Throwable cause) {
    super(message, cause);
  }
}
