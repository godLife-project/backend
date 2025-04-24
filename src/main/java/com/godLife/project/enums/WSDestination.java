package com.godLife.project.enums;

import lombok.Getter;

@Getter
public enum WSDestination {

  // PUBLISHER

  // SUBSCRIBER
  SUB_GET_MATCHED_QNA_LIST("/queue/matched/qna"),
  SUB_GET_WAIT_QNA_LIST("/sub/waitList"),
  SUB_QNA_MATCH_RESULT("/queue/isMatched/waitQna"),
  SUB_GET_QNA_DETAIL("/queue/set/qna/detail/");

  private final String destination;

  WSDestination(String destination) {
    this.destination = destination;
  }

}
