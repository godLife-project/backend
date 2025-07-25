package com.godLife.project.service.impl.statisticsImpl;

import com.godLife.project.dto.statistics.DayAndMonthStats;
import com.godLife.project.dto.statistics.QnaAdminStat;
import com.godLife.project.dto.statistics.response.ResponseQnaAdminStat;
import com.godLife.project.enums.QnaStatus;
import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.StatsMapper.ServiceAdminStatMapper;
import com.godLife.project.service.interfaces.statistics.ServiceAdminStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceAdminStatServiceImpl implements ServiceAdminStatService {

  private final ServiceAdminStatMapper serviceAdminStatMapper;


  // 문의 처리 통계 초기 데이터 세팅 메소드
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void setInitServiceAdminStat(int userIdx) {
    try {
      // 일간 통계 데이터 존재 여부 확인
      boolean dailyStatIsExist = serviceAdminStatMapper.existsQnaDailyStatsByAdminIdx(userIdx) > 0;

      log.info("dailyStats 존재 여부: {}", dailyStatIsExist ? "데이터 있음" : "데이터 없음");
      if (!dailyStatIsExist) {
        serviceAdminStatMapper.setQnaDailyStatsByAdminIdx(userIdx);
        log.info("일별 문의 처리 통계 초기 데이터 세팅 완료.");
      }

      // 월간 통계 데이터 존재 여부 확인
      boolean monthStatIsExist = serviceAdminStatMapper.existsQnaMonthStatsByAdminIdx(userIdx) > 0;

      log.info("monthStats 존재 여부: {}", monthStatIsExist ? "데이터 있음" : "데이터 없음");
      if (!monthStatIsExist) {

        String currentMonth = new SimpleDateFormat("yyyy-MM").format(new Date());
        serviceAdminStatMapper.setQnaMonthStatsByAdminIdx(currentMonth, userIdx);
        log.info("월별 문의 처리 통계 초기 데이터 세팅 완료.");
      }

      // 요약 통계 데이터 존재 여부 확인
      boolean summaryIsExist = serviceAdminStatMapper.existQnaAdminSummaryByAdminIdx(userIdx) > 0;

      log.info("summaryStats 존재 여부: {}", summaryIsExist ? "데이터 있음" : "데이터 없음");
      if (!summaryIsExist) {
        serviceAdminStatMapper.setQnaAdminSummaryStatsByAdminIdx(userIdx);
        log.info("요약 문의 처리 통계 초기 데이터 세팅 완료.");
      }

      // 새 행이 하나라도 추가가 된다면? ::> 오늘 출근하고 처음 들어왔다는 뜻
      if (!dailyStatIsExist || !monthStatIsExist || !summaryIsExist) {
        serviceAdminStatMapper.increaseQnaAdminWorkingDay(userIdx); // 출근 일 수 증가
      }

    } catch (Exception e) {
      log.error("setInitServiceAdminStat 실행 중 예외 발생");
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw e;
    }
  }

  // 통계 데이터 조회
  @Override
  public ResponseQnaAdminStat getQnaAdminStats(int adminIdx) {
    try {
      // 일/월간 누적 데이터 조회
      DayAndMonthStats tempDTO = serviceAdminStatMapper.getQnaAdminDayAndMonthStats(adminIdx);
      // 통계 요약 데이터 조회
      ResponseQnaAdminStat statistics = serviceAdminStatMapper.getQnaAdminStatsSummary(adminIdx);

      // 통계 요약에 없는 일/월간 누적 데이터 세팅
      statistics.setMyTodayTotal(tempDTO.getDTotalCount());
      statistics.setMyMonthTotal(tempDTO.getMTotalCount());
      statistics.setMyMonthDuration(tempDTO.getMTotalDuration());

      return statistics;
    } catch (Exception e) {
      log.error("ServiceAdminStatService - getQnaAdminStatsSummary :: 서버 오류 발생", e);
      throw e;
    }
  }

  // 통계 데이터 최신화
  @Override
  public ResponseQnaAdminStat updateQnaAdminSummaryStats(int qnaIdx, int adminIdx) {
    try {
      // 초기 데이터 조회
      QnaAdminStat initQnaAdminStats = serviceAdminStatMapper.getQnaAdminStatsAll(adminIdx);
      if (initQnaAdminStats == null) {
        setInitServiceAdminStat(adminIdx); // 없을 경우 행 추가
        log.info("ServiceAdminStatService - updateQnaAdminSummaryStats :: 초기 데이터가 없어 행 추가됨..");
        // 객체가 null 일 테니 새로 생성 (굳이 매퍼로 다시 가져오지 않음)
        initQnaAdminStats = new QnaAdminStat();
        initQnaAdminStats.setAdminIdx(adminIdx);
      }

      String findStatus = QnaStatus.COMPLETE.getStatus();
      int duration = serviceAdminStatMapper.getCompleteQnaDurationByQnaIdx(qnaIdx, findStatus); // 경과 시간 조회

      // 일간, 월간 통계 데이터 일괄 최신화 후 수정된 데이터 조회
      DayAndMonthStats dayAndMonthStats =  updateQnaStatsAtDailyAndMonth(adminIdx, duration);

      // 요약 통계 최신화 용 DTO 생성
      QnaAdminStat updateSummaryStatsDTO = new QnaAdminStat();
      updateSummaryStatsDTO.setAdminIdx(adminIdx); // adminIdx 세팅

      updateSummaryStatsDTO.setTotalCount(initQnaAdminStats.getTotalCount() + 1); // 누적 처리 문의 수 증가
      updateSummaryStatsDTO.setTotalDuration(initQnaAdminStats.getTotalDuration() + duration); // 누적 처리 문의 소요 시간 증가

      int workingDay = initQnaAdminStats.getWorkingDays(); // 총 출근 횟수 조회
    /*    --  avgQnaPerDay :: 하루 평균 처리 건 수  --
      하루 평균 처리 건 수 = 전체 문의 처리 건 수 / 총 출근 횟수
      ArithmeticException (분모가 0) 을 방지하기 위해 workingDay 가 0일 경우, 원래 있던 데이터로 값 고정
     */
      int avgQnaPerDay = workingDay == 0 ? initQnaAdminStats.getAvgQnaPerDay() : updateSummaryStatsDTO.getTotalCount() / workingDay;
      updateSummaryStatsDTO.setAvgQnaPerDay(avgQnaPerDay);

    /*    --  avgDurationPerDay :: 하루 평균 문의 처리 소요 시간  --
      하루 평균 문의 처리 소요 시간 = 오늘 하루 누적 소요 시간 / 오늘 하루 누적 문의 처리 건 수
      ArithmeticException (분모가 0) 을 방지하기 위해 dayAndMonthStats.getDTotalCount() 이 0일 경우, 원래 있던 데이터로 값 고정
     */
      int avgDurationPerDay = dayAndMonthStats.getDTotalCount() == 0 ? initQnaAdminStats.getAvgDurationPerDay() :  dayAndMonthStats.getDTotalDuration() / dayAndMonthStats.getDTotalCount();
      updateSummaryStatsDTO.setAvgDurationPerDay(avgDurationPerDay);

    /*    --  avgQnaPerMonth :: 이번 달 평균 문의 처리량  --
      이번 달 평균 문의 처리량 = 전체 누적 문의 처리 수 / 총 개월 수
      ArithmeticException (분모가 0) 을 방지하기 위해 dayAndMonthStats.getMonthCount() 이 0일 경우, 원래 있던 데이터로 값 고정
     */
      int avgQnaPerMonth = dayAndMonthStats.getMonthCount() == 0 ? initQnaAdminStats.getAvgQnaPerMonth() : updateSummaryStatsDTO.getTotalCount() / dayAndMonthStats.getMonthCount();
      updateSummaryStatsDTO.setAvgQnaPerMonth(avgQnaPerMonth);

    /*    --  avgDurationPerMonth :: 이번 달 평균 문의 처리 소요 시간  --
      이번 달 평균 문의 처리 소요 시간 = 이번 달 누적 문의 처리 소요 시간 / 이번 달 총 문의 처리 횟수
      ArithmeticException (분모가 0) 을 방지하기 위해 dayAndMonthStats.getMTotalCount() 이 0일 경우, 원래 있던 데이터로 값 고정
     */
      int avgDurationPerMonth = dayAndMonthStats.getMTotalCount() == 0 ? initQnaAdminStats.getAvgDurationPerMonth() : dayAndMonthStats.getMTotalDuration() / dayAndMonthStats.getMTotalCount();
      updateSummaryStatsDTO.setAvgDurationPerMonth(avgDurationPerMonth);

      // 최근 처리 문의 소요 시간 최신화
      updateSummaryStatsDTO.setRecentDuration(duration);

      // 통계 업데이트
      serviceAdminStatMapper.updateQnaAdminSummaryStats(updateSummaryStatsDTO);

      ResponseQnaAdminStat qnaAdminStat = new ResponseQnaAdminStat();
      qnaAdminStat.setAvgQnaPerDay(updateSummaryStatsDTO.getAvgQnaPerDay());
      qnaAdminStat.setAvgDurationPerDay(updateSummaryStatsDTO.getAvgDurationPerDay());
      qnaAdminStat.setAvgQnaPerMonth(updateSummaryStatsDTO.getAvgQnaPerMonth());
      qnaAdminStat.setAvgDurationPerMonth(updateSummaryStatsDTO.getAvgDurationPerMonth());

      qnaAdminStat.setRecentDuration(updateSummaryStatsDTO.getRecentDuration());
      qnaAdminStat.setMyTodayTotal(dayAndMonthStats.getDTotalCount());
      qnaAdminStat.setMyMonthTotal(dayAndMonthStats.getMTotalCount());
      qnaAdminStat.setMyMonthDuration(dayAndMonthStats.getMTotalDuration());

      return qnaAdminStat;
    } catch (Exception e) {
      log.error("ServiceAdminStatService - updateQnaAdminSummaryStats :: 서버 오류 발생", e);
      throw new CustomException("통계 데이터 최신화 중 서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // 일간, 월간 통계 데이터 수정
  @Transactional
  public DayAndMonthStats updateQnaStatsAtDailyAndMonth(int adminIdx, int duration) {
    serviceAdminStatMapper.updateQnaDailyStatsByAdminIdx(adminIdx, duration);
    serviceAdminStatMapper.updateQnaMonthStatsByAdminIdx(adminIdx, duration);

    return serviceAdminStatMapper.getQnaAdminDayAndMonthStats(adminIdx);
  }

}
