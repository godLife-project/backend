package com.godLife.project.exception;

import lombok.Getter;

@Getter
public class WebSocketBusinessException extends RuntimeException {

  private final int code;
  private final String username;

  public WebSocketBusinessException(String message, int code, String username) {
    super(message);
    this.code = code;
    this.username = username;
  }
}
