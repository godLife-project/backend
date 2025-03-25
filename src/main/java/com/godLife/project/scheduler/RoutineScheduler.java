package com.godLife.project.scheduler;

import com.godLife.project.service.impl.scheduleImpl.RoutineScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoutineScheduler {

  private final RoutineScheduleServiceImpl routineScheduleService;

  /*
    [Cron 표현식 설명]

    * 1. 매일 자정 00:00:00에 실행
       "0 0 0 * * ?"

    * 2. 2시간마다 실행 (0시, 2시, 4시 ...)
       "0 0 0/2 * * ?"

    * 3. 매주 월요일 자정 00:00:00에 실행
       "0 0 0 ? * MON"

    * 4. 주중(월~금) 매일 오전 9시에 실행
       "0 0 9 ? * MON-FRI"

    * 5. 매월 마지막 날 자정 00:00:00에 실행
       "0 0 0 L * ?"

    [Cron 표현식 구조]
       (초) (분) (시) (일) (월) (요일)

       - 초: 0 ~ 59
       - 분: 0 ~ 59
       - 시: 0 ~ 23
       - 일: 1 ~ 31
       - 월: 1 ~ 12 또는 JAN ~ DEC
       - 요일: 1 ~ 7 (일: 1, 월: 2, ..., 토: 7) 또는 SUN ~ SAT

    [특수 문자]
       - * : 모든 값
       - ? : 특정 값 없음 (일/요일 중 하나만 설정할 때 사용)
       - / : 증가 간격 (0/2는 0부터 시작해 2 간격으로 실행)
       - , : 여러 값 지정 (1,3,5는 1, 3, 5에 실행)
       - - : 범위 지정 (10-12는 10, 11, 12에 실행)
       - L : 마지막 날 (달의 마지막 날 또는 요일의 마지막 날)
*/

  // 🔥 매일 자정에 실행 (00:00:00)
  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional(rollbackFor = Exception.class)
  public void dailyRoutineCheck() {
    try {
      // 인증률 90퍼 미만 루틴 경험치 감소 로직
      routineScheduleService.verifyAndDecreaseExpIfUnder90();
      System.out.println("1...인증률 90% 미만 루틴의 경험치를 일괄 감소 했습니다.");
      routineScheduleService.clearComboWhenAllFireIsActivatedOfPlan();
      System.out.println("2...진행중인 루틴의 불꽃을 모두 활성화 하지 못한 유저의 콤보를 초기화 했습니다.");
      routineScheduleService.clearAllFireStateWhenMidnight();
      System.out.println("3...진행중인 루틴의 불꽃 활성화 상태를 초기화 했습니다.");
      int AccountClearResult = routineScheduleService.clearAccountDelete();
      System.out.println("4...탈퇴한 유저중 데이터 유지 만료일이 지난 계정을 삭제 처리했습니다.....삭제된 유저 수 ::> " + AccountClearResult);
    } catch (Exception e) {
      log.error("e: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
    }

  }

  // ⚡ 2시간마다 실행 (0시, 2시, 4시, ...)
  @Scheduled(cron = "0 0 0/2 * * ?")
  public void checkEveryTwoHours() {
    int result = routineScheduleService.deleteExpiredRefreshTokens();
    System.out.println(".....만료 된 모든 재발급 토큰을 삭제했습니다.....삭제된 토큰 수 ::> " + result);
  }
}
