package com.godLife.project.exception;

import lombok.Getter;

@Getter
public class WebSocketBusinessException extends RuntimeException {

  private final int code;

  public WebSocketBusinessException(String message, int code) {
    super(message);
    this.code = code;
  }
}
