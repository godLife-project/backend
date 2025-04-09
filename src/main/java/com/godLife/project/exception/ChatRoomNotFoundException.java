package com.godLife.project.exception;

public class ChatRoomNotFoundException extends RuntimeException {
  public ChatRoomNotFoundException(String message) {
    super(message);
  }
}
