package com.godLife.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
  private final HttpStatus status;
  private final String path;

  public CustomException(String message, HttpStatus status, String path) {
    super(message);
    this.status = status;
    this.path = path;
  }

}
