package com.godLife.project.enums;

import lombok.Getter;

@Getter
public enum WSDestination {

  // PUBLISHER

  // SUBSCRIBER
  SUB_GET_MATCHED_QNA_LIST("/queue/matched/qna");

  private final String destination;

  WSDestination(String destination) {
    this.destination = destination;
  }

}
