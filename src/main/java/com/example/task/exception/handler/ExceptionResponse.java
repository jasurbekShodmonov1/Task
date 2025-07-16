package com.example.task.exception.handler;



import com.example.task.exception.AlreadyExistsException;
import com.example.task.exception.ApiException;
import org.immutables.builder.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.NoSuchElementException;

import static com.example.task.exception.handler.ErrorCodes.*;


public record ExceptionResponse(
    int code, String status, String path, String message, String timestamp) {

  @Builder.Constructor
  public ExceptionResponse(final Exception exception, final String path, final HttpStatus status) {
    this(
        findErrorCode(exception),
        String.format("%d %s", status.value(), status.getReasonPhrase()),
        path,
        exception.getMessage(),
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
            .withZone(ZoneId.of("UTC+5"))
            .format(Instant.now()));
  }

  private static int findErrorCode(final Exception e) {
    if (e instanceof ApiException) {
      return API_ERROR_CODE;
    }

    if (e instanceof AlreadyExistsException) {
      return ALREADY_EXISTS_ERROR_CODE;
    }

    if (e instanceof NoSuchElementException) {
      return NOT_FOUND_ERROR_CODE;
    }

    if (e instanceof NullPointerException) {
      return NULL_POINTER_ERROR_CODE;
    }
    return 0;
  }
}
