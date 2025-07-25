package com.godLife.project.service.interfaces.statistics;

import com.godLife.project.dto.statistics.response.ResponseQnaAdminStat;

public interface ServiceAdminStatService {

  /**
   * <strong>문의 처리 통계 초기 데이터 세팅 메소드</strong>
   * <p>초기 데이터 유무를 내부적으로 검증 후 초기 데이터를 추가합니다.</p>
   * @param userIdx 저장 할 상담원의 인덱스 번호
   */
  void setInitServiceAdminStat(int userIdx);

  /**
   * <strong>상담원 문의 처리 현황 조회</strong>
   * <p>avgQnaPerDay : 하루 평균 처리 건 수</p>
   * <p>avgDurationPerDay : 하루 평균 문의 처리 소요 시간</p>
   * <p>avgQnaPerMonth : 매 달 평균 문의 처리량</p>
   * <p>avgDurationPerMonth : 매 달 평균 문의 처리 소요 시간</p>
   * <p>recentDuration : 최근 완료된 문의의 소요 시간</p>
   * @param adminIdx 조회 할 상담원 인덱스 번호
   * @return {@code ResponseQnaAdminStat}
   */
  ResponseQnaAdminStat getQnaAdminStats(int adminIdx);

  /**
   * <strong>상담원 일간/월간/요약 통계 전체 최신화</strong>
   * <p>문의 완료 처리가 될 경우 통계 데이터 전체 최신화 합니다</p>
   * <p>최신화 후 다시 매퍼를 통해 최신 정보를 조회할 필요 없이, 바로 최신 DTO를 반환 해줍니다.</p>
   * @param qnaIdx 완료 처리 된 문의의 인덱스 번호
   * @param adminIdx 최신화 해줄 상담원의 인덱스 번호
   * @return {@code ResponseQnaAdminStat}
   */
  ResponseQnaAdminStat updateQnaAdminSummaryStats(int qnaIdx, int adminIdx);

}
