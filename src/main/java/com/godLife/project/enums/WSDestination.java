package com.godLife.project.enums;

import lombok.Getter;

@Getter
public enum WSDestination {

  // PUBLISHER

  // SUBSCRIBER
  /**
   * 매칭된 문의 리스트 전송 경로 (단일)
   */
  QUEUE_MATCHED_QNA_LIST("/queue/matched/qna"),
  /**
   * 대기중 문의 리스트 전송 경로 (전체)
   */
  ALL_WAIT_QNA_LIST("/sub/waitList"),
  /**
   * 할당 메세지 전송 경로 (단일)
   */
  QUEUE_QNA_MATCH_RESULT("/queue/isMatched/waitQna"),
  /**
   * 문의 상세 정보 전송 경로 (단일)
   */
  QUEUE_QNA_DETAIL("/queue/set/qna/detail/"),
  /**
   * 서비스센터(1:1문의 관리자페이지) 접속한 관리자 목록 전송 경로 (전체)
   */
  ALL_ACCESS_ADMIN_LIST("/sub/access/list"),
  /**
   * 완료된 문의 리스트 전송 경로 (단일)
   */
  QUEUE_COMPLETE_QNA_LIST("/queue/completed/qna/list"),
  /**
   * 1:1 문의 상담원 문의 처리 현황 경로 (단일)
   */
  QUEUE_QNA_ADMIN_STATISTICS("/queue/qna/admin/statistics");

  private final String destination;

  WSDestination(String destination) {
    this.destination = destination;
  }

}
