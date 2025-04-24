package com.godLife.project.enums;

import lombok.Getter;

@Getter
public enum MessageStatus {

  RELOAD("RELOAD"),
  ADD("ADD"),
  REMOVE("REMOVE"),
  UPDATE("UPDATE"),

  // 문의 본문과 댓글 관리 상태값
  MODIFY_BODY("MOD_BODY"),
  MODIFY_COMM("MOD_COMM"),
  ADD_COMMENT("ADD_COMM"),
  DELETE_COMM("DEL_COMM");

  private final String status;

  MessageStatus(String status) {
    this.status = status;
  }
}
