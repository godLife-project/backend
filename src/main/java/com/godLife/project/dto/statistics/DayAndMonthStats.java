package com.godLife.project.dto.statistics;

import lombok.Getter;
import lombok.Setter;

/**
 * 조회용 DTO - 상담원 일간/월간 통계 데이터 조회
 * <p>{@code dTotalCount} 일간 누적 문의 처리 수</p>
 * <p>{@code dTotalDuration} 일간 누적 문의 처리 소요 시간</p>
 * <p>{@code mTotalCount} 월간 누적 문의 처리 수</p>
 * <p>{@code mTotalDuration} 월간 누적 문의 처리 소요 시간</p>
 * <p>{@code monthCount} 상담원 활동 개월 수</p>
 */
@Getter
@Setter
public class DayAndMonthStats {
  // 일간 누적 문의 처리 수
  private int dTotalCount;
  // 일간 누적 문의 처리 소요 시간
  private int dTotalDuration;
  // 월간 누적 문의 처리 수
  private int mTotalCount;
  // 월간 누적 문의 처리 소요 시간
  private int mTotalDuration;
  // 상담원 활동 개월 수
  private int monthCount;
}
