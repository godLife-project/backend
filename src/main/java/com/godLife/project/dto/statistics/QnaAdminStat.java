package com.godLife.project.dto.statistics;

import com.godLife.project.dto.statistics.response.ResponseQnaAdminStat;
import lombok.Getter;
import lombok.Setter;

/**
 * 조회용 DTO - 상담원 통계 데이터 전체 조회
 * <p>{@code adminIdx} 상담원 인덱스</p>
 * <p>{@code totalCount} 총 누적 문의 처리량</p>
 * <p>{@code totalDuration} 총 누적 문의 처리 소요 시간</p>
 * <p>{@code workingDays} 실 근무일 (로그인 후 서비스 센터 입장 시점)</p>
 * <p>{@code avgQnaPerDay} 하루 평균 처리 건 수</p>
 * <p>{@code avgDurationPerDay} 하루 평균 문의 처리 소요 시간</p>
 * <p>{@code avgQnaPerMonth} 매 달 평균 문의 처리량</p>
 * <p>{@code avgDurationPerMonth} 매 달 평균 문의 처리 소요 시간</p>
 * <p>{@code recentDuration} 최근 완료된 문의의 소요 시간</p>
 * <p>{@code myTodayTotal} 오늘 하루 총 문의 처리량</p>
 * <p>{@code myMonthTotal} 이번달 총 문의 처리량</p>
 * <p>{@code myMonthDuration} 이번달 총 문의 처리 소요 시간</p>
 */
@Getter
@Setter
public class QnaAdminStat extends ResponseQnaAdminStat {

  // 상담원 인덱스
  private int adminIdx;
  // 총 누적 문의 처리량
  private int totalCount;
  // 총 누적 문의 처리 소요 시간
  private int totalDuration;
  // 실 근무일 (로그인 후 서비스 센터 입장 시점)
  private int workingDays;

  public QnaAdminStat() {
    super();
  }
}
