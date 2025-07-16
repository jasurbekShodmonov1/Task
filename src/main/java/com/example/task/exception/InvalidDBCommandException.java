package com.example.task.exception;

public class InvalidDBCommandException extends RuntimeException {
  public InvalidDBCommandException(String message) {
    super(message);
  }

  public InvalidDBCommandException(String message, Throwable cause) {
    super(message, cause);
  }
}
