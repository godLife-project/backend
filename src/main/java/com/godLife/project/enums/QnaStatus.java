package com.godLife.project.enums;

import lombok.Getter;

@Getter
public enum QnaStatus {

  WAIT("WAIT"),
  CONNECT("CONNECT"),
  RESPONDING("RESPONDING"),
  COMPLETE("COMPLETE"),
  SLEEP("SLEEP"),
  DELETED("DELETED");

  private final String status;

  QnaStatus(String status) {
    this.status = status;
  }
}
