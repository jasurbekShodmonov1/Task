package com.example.task.exception.handler;

import org.springframework.http.HttpStatus;

public class ExceptionResponseBuilder {
  private String path;
  private HttpStatus status;
  private Exception exception;

  public ExceptionResponseBuilder path(String path) {
    this.path = path;
    return this;
  }

  public ExceptionResponseBuilder status(HttpStatus status) {
    this.status = status;
    return this;
  }

  public ExceptionResponseBuilder exception(Exception exception) {
    this.exception = exception;
    return this;
  }

  public ExceptionResponse build() {
    return new ExceptionResponse(exception, path, status);
  }
}
