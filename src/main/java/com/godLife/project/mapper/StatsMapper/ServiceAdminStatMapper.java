package com.godLife.project.mapper.StatsMapper;

import com.godLife.project.dto.statistics.DayAndMonthStats;
import com.godLife.project.dto.statistics.QnaAdminStat;
import com.godLife.project.dto.statistics.response.ResponseQnaAdminStat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ServiceAdminStatMapper {

  /**
   * 일별 문의 처리 통계 테이블에 데이터 존재 유무 조회
   * @param adminIdx 데이터 존재 유무를 조회 할 상담원의 인덱스 번호
   * @return {@code int} 0 혹은 1
   */
  @Select("SELECT COUNT(*) FROM QNA_DAILY_STATS WHERE STAT_DATE = TRUNC(SYSDATE) AND ADMIN_IDX = #{adminIdx}")
  int existsQnaDailyStatsByAdminIdx(int adminIdx);

  /**
   * 월별 문의 처리 통계 테이블에 데이터 존재 유무 조회
   * @param adminIdx 데이터 존재 유무를 조회 할 상담원의 인덱스 번호
   * @return {@code int} 0 혹은 1
   */
  @Select("SELECT COUNT(*) FROM QNA_MONTH_STATS WHERE STAT_MONTH = TO_CHAR(SYSDATE, 'YYYY-MM') AND ADMIN_IDX = #{adminIdx}")
  int existsQnaMonthStatsByAdminIdx(int adminIdx);

  /**
   * 상담원 통계 요약 테이블에 데이터 존재 유무 조회
   * @param adminIdx 데이터 존재 유무를 조회 할 상담원의 인덱스 번호
   * @return {@code int} 0 혹은 1
   */
  @Select("SELECT COUNT(*) FROM QNA_ADMIN_SUMMARY_STATS WHERE ADMIN_IDX = #{adminIdx}")
  int existQnaAdminSummaryByAdminIdx(int adminIdx);


  /**
   * 일별 상담원 문의 처리 통계 초기 데이터 저장
   * @param adminIdx 초기 데이터를 추가 할 상담원의 인덱스 번호
   */
  @Insert("INSERT INTO QNA_DAILY_STATS(ADMIN_IDX) VALUES(#{adminIdx})")
  void setQnaDailyStatsByAdminIdx(int adminIdx);

  /**
   * 월별 상담원 문의 처리 통계 초기 데이터 저장
   * @param adminIdx 초기 데이터를 추가 할 상담원의 인덱스 번호
   */
  @Insert("INSERT INTO QNA_MONTH_STATS(ADMIN_IDX) VALUES(#{adminIdx})")
  void setQnaMonthStatsByAdminIdx(int adminIdx);

  /**
   * 요약 상담원 문의 처리 통계 초기 데이터 저장
   * @param adminIdx 초기 데이터를 추가 할 상담원의 인덱스 번호
   */
  @Insert("INSERT INTO QNA_ADMIN_SUMMARY_STATS(ADMIN_IDX) VALUES(#{adminIdx})")
  void setQnaAdminSummaryStatsByAdminIdx(int adminIdx);

  /**
   * 상담원의 문의 처리 현황을 조회합니다.
   * <p>AVG_QNA_PER_DAY : 하루 평균 처리 건 수</p>
   * <p>AVG_DURATION_PER_DAY : 하루 평균 문의 처리 소요 시간</p>
   * <p>AVG_QNA_PER_MONTH : 매 달 평균 문의 처리량</p>
   * <p>AVG_DURATION_PER_MONTH : 매 달 평균 문의 처리 소요 시간</p>
   * <p>RECENT_DURATION : 최근 완료된 문의의 소요 시간</p>
   * @param adminIdx 조회 할 상담원의 인덱스 번호
   * @return {@code ResponseQnaAdminStat}
   */
  @Select("""
      SELECT AVG_QNA_PER_DAY, AVG_DURATION_PER_DAY, AVG_QNA_PER_MONTH, AVG_DURATION_PER_MONTH, RECENT_DURATION
        FROM QNA_ADMIN_SUMMARY_STATS
       WHERE ADMIN_IDX = #{adminIdx}""")
  ResponseQnaAdminStat getQnaAdminStatsSummary(int adminIdx);

  /**
   * 상담원 문의 처리 현황 전체 조회
   * <p>{@code adminIdx} 상담원 인덱스</p>
   * <p>{@code totalCount} 총 누적 문의 처리량</p>
   * <p>{@code totalDuration} 총 누적 문의 처리 소요 시간</p>
   * <p>{@code workingDays} 실 근무일 (로그인 후 서비스 센터 입장 시점)</p>
   * <p>{@code avgQnaPerDay} 하루 평균 처리 건 수</p>
   * <p>{@code avgDurationPerDay} 하루 평균 문의 처리 소요 시간</p>
   * <p>{@code avgQnaPerMonth} 매 달 평균 문의 처리량</p>
   * <p>{@code avgDurationPerMonth} 매 달 평균 문의 처리 소요 시간</p>
   * <p>{@code recentDuration} 최근 완료된 문의의 소요 시간</p>
   * @param adminIdx 조회 할 상담원의 인덱스 번호
   * @return {@code QnaAdminStat}
   */
  @Select("SELECT * FROM QNA_ADMIN_SUMMARY_STATS WHERE ADMIN_IDX = #{adminIdx}")
  QnaAdminStat getQnaAdminStatsAll(int adminIdx);

  /**
   * 지정한 문의의 Responding 상태 -> Complete 상태가 되기 까지 소요된 시간을 조회합니다.
   * @param qnaIdx 조회할 문의의 인덱스 번호
   * @param findStatus 어떤 상태의 문의를 조회 할 지 설정
   * @return {@code int}
   */
  @Select("SELECT FLOOR((SYSDATE - RESPONDING_DATE) * 86400) AS DURATION FROM QNA_TABLE WHERE QNA_IDX = #{qnaIdx} AND QNA_STATUS = #{findStatus}")
  int getCompleteQnaDurationByQnaIdx(int qnaIdx, String findStatus);

  /**
   * 특정 상담원의 일간 통계 데이터를 업데이트 합니다.
   * <p>{@code totalCount} 와 {@code totalDuration} 이 올라갑니다.</p>
   * @param adminIdx 수정 할 상담원의 인덱스 번호
   * @param duration 누적 할 문의 소요 시간
   */
  void updateQnaDailyStatsByAdminIdx(int adminIdx, int duration);

  /**
   * 특정 상담원의 월간 통계 데이터를 업데이트 합니다.
   * <p>{@code totalCount} 와 {@code totalDuration} 이 올라갑니다.</p>
   * @param adminIdx 수정 할 상담원의 인덱스 번호
   * @param duration 누적 할 문의 소요 시간
   */
  void updateQnaMonthStatsByAdminIdx(int adminIdx, int duration);

  /**
   * QNA_ADMIN_SUMMARY_STATS 테이블에서 상담원 출근 일 수를 업데이트 합니다.
   * @param adminIdx 업데이트 할 상담원의 인덱스 번호
   */
  @Update("UPDATE QNA_ADMIN_SUMMARY_STATS SET WORKING_DAYS = WORKING_DAYS + 1 WHERE ADMIN_IDX = #{adminIdx}")
  void increaseQnaAdminWorkingDay(int adminIdx);

  /**
   * 상담원의 일간/월간 통계 데이터를 조회합니다.
   * <p>단, 지정 날짜가 아닌 현재 시점 기준입니다.</p>
   * <p>(2025/05/25 에 조회 할 경우, 25일 누적 데이터 + 25년 5월간 누적 데이터 조회)</p>
   * @param adminIdx 조회 할 상담원의 인덱스 번호
   * @return {@code DayAndMonthStats} 하루 동안 누적된 문의 수,소요 시간 + 이번 달 동안 누적된 문의 수,소요 시간 + 활동 개월 수
   */
  DayAndMonthStats getQnaAdminDayAndMonthStats(int adminIdx);

  /**
   * 상담원 통계 요약 테이블의 정보를 업데이트 합니다.
   * <p>업데이트 할 정보가 DTO에 담겨 있어야 하며,
   * {@code adminIdx} 와 {@code workingDays}를 제외한 모든 컬럼을 set 하기 때문에 주의 해야 합니다.</p>
   *
   * @param updateDTO 업데이트 할 {@code QnaAdminStat} 입니다
   */
  void updateQnaAdminSummaryStats(QnaAdminStat updateDTO);

}
