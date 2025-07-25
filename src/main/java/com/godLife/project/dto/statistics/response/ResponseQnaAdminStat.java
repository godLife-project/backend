package com.godLife.project.dto.statistics.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 응답용 DTO - 상담원 문의 처리 현황
 * <p>{@code avgQnaPerDay} 하루 평균 처리 건 수</p>
 * <p>{@code avgDurationPerDay} 하루 평균 문의 처리 소요 시간</p>
 * <p>{@code avgQnaPerMonth} 매 달 평균 문의 누적 처리량</p>
 * <p>{@code avgDurationPerMonth} 매 달 평균 문의 처리 소요 시간</p>
 * <p>{@code recentDuration} 최근 완료된 문의의 소요 시간</p>
 * <p>{@code myTodayTotal} 오늘 하루 총 문의 처리량</p>
 * <p>{@code myMonthTotal} 이번달 총 문의 처리량</p>
 * <p>{@code myMonthDuration} 이번달 총 문의 처리 소요 시간</p>
 */
@Getter
@Setter
public class ResponseQnaAdminStat {

  // 하루 평균 처리 건 수
  private int avgQnaPerDay;
  // 하루 평균 문의 처리 소요 시간
  private int avgDurationPerDay;
  // 매 달 평균 문의 누적 처리량
  private int avgQnaPerMonth;
  // 매 달 평균 문의 처리 소요 시간
  private int avgDurationPerMonth;

  // 최근 완료된 문의의 소요 시간
  private int recentDuration;
  // 오늘 하루 문의 처리량
  private int myTodayTotal;
  // 이번달 누적 문의 처리량
  private int myMonthTotal;
  // 이번달 누적 소요 시간
  private int myMonthDuration;
}
