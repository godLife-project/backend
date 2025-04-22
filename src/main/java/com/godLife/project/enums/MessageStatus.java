package com.godLife.project.enums;

import lombok.Getter;

@Getter
public enum MessageStatus {

  RELOAD("RELOAD"),
  ADD("ADD"),
  REMOVE("REMOVE"),
  UPDATE("UPDATE");

  private final String status;

  MessageStatus(String status) {
    this.status = status;
  }
}
