package com.godLife.project.enums;

import lombok.Getter;

@Getter
public enum QnaRedisKey {
  /**
   * 문의 등록 시 레디스 큐에 올리기 위한 키 값
   */
  QNA_QUEUE_KEY("qna_queue"),
  /**
   * 상담원이 해당 문의를 조회 중인지 확인하기 위한 키 값
   * <p>{@code qnaIdx} 와 같이 사용 해야 함.</p>
   */
  QNA_WATCHER("qna-watcher-"),
  /**
   * 1:1문의 할당 중 동시성 이슈를 방지하기 위한 문의 잠금 장치
   * <p>{@code qnaIdx} 와 같이 사용 해야 함.</p>
   */
  QNA_LOCK("qna-lock:"),
  /**
   * 상담원이 답변을 남겼을 경우 해당 문의의 상태를 추적하기 위한 키 값
   * <p>{@code qnaIdx} 와 같이 사용 해야 함.</p>
   */
  QNA_ADMIN_ANSWERED("qna-admin-answered::");

  private final String key;

  QnaRedisKey(String key) { this.key = key; }


}
