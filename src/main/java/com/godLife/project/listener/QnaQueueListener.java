package com.godLife.project.listener;

import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.serviceAdmin.AdminIdxAndIdDTO;
import com.godLife.project.enums.WSDestination;
import com.godLife.project.mapper.AdminMapper.ServiceAdminMapper;
import com.godLife.project.mapper.autoMatch.AutoMatchMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.impl.websocketImpl.WebSocketMessageService;
import com.godLife.project.service.interfaces.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class QnaQueueListener implements InitializingBean, DisposableBean {

  private final RedisService redisService;
  private final AutoMatchMapper autoMatchMapper;
  private final ServiceAdminMapper serviceAdminMapper;
  private final WebSocketMessageService messageService;
  private final QnaService qnaService;

  private static final String QNA_QUEUE_KEY = "qna_queue";

  private static final Object lock = new Object();

  private int restartCount = 0;

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  @Override
  public void afterPropertiesSet() {
    Thread qnaAutoMatchThread = new Thread(() -> {
      synchronized (lock) {
        while (true) {
          try {

            if (restartCount >= 3) {
              log.info("QnaQueueListener - autoMatch :: 장시간 새로운 문의가 들어오지 않았습니다. 10분간 자동매칭을 멈춥니다.");
              // 10분 후 깨워줄 스레드 생성
              scheduler.schedule(() -> {
                synchronized (lock) {
                  lock.notify();
                }
              }, 10, TimeUnit.MINUTES);

              synchronized (lock) {
                lock.wait(); // 잠깐 대기
              }
              restartCount = 0;
            }

            String result = redisService.brPopFromRedisQueue(QNA_QUEUE_KEY, 0);

            if (result != null) {
              int qnaIdx = Integer.parseInt(result);
              AdminIdxAndIdDTO adminInfo = autoMatchMapper.getServiceAdminIdx(); // 매칭 가능 상담원 조회

              if (adminInfo != null) {
                autoMatchMapper.autoMatchSingleQna(qnaIdx, adminInfo.getUserIdx()); // 상담원 자동 매칭 시도
                serviceAdminMapper.setMatchedByQuestionCount(adminInfo.getUserIdx()); // 매칭된 상담원 매칭 문의 수 증가
                List<QnaMatchedListDTO> matchedList = qnaService.getlistAllMatchedQna(adminInfo.getUserIdx());

                // 관리자에게 매칭 리스트 업데이트
                messageService.sendToUser(adminInfo.getUserId(), WSDestination.SUB_GET_MATCHED_QNA_LIST.getDestination(), matchedList);

                log.info("QnaQueueListener - autoMatch :: 문의 담당자에게 매칭되었습니다. ::> 매칭된 문의 - {} / 담당자 - {}", qnaIdx ,adminInfo);

              }
              else {
                log.info("QnaQueueListener - autoMatch :: 매칭 가능한 문의 담당자가 없습니다. 5분 후 자동 매칭을 재개합니다.");
                redisService.rightPushToRedisQueue(QNA_QUEUE_KEY, String.valueOf(qnaIdx));

                // 5분 후 깨워줄 스레드 생성
                scheduler.schedule(() -> {
                  synchronized (lock) {
                    lock.notify();
                  }
                }, 5, TimeUnit.MINUTES);

                synchronized (lock) {
                  lock.wait(); // 잠깐 대기
                }
              }
            }
          } catch (QueryTimeoutException e) {
            log.info("QnaQueueListener - autoMatch :: 1분 동안 새로 들어온 문의가 없습니다..10초 후 자동 매칭을 다시 시도 합니다.");
            try {
              // 10초 후 깨워줄 스레드 생성
              scheduler.schedule(() -> {
                synchronized (lock) {
                  lock.notify();
                }
              }, 10, TimeUnit.SECONDS);

              synchronized (lock) {
                lock.wait(); // 잠깐 대기
              }
              restartCount = restartCount + 1;
            } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
              log.info("QnaQueueListener - autoMatch :: 스레드 상태 복구 후 종료 처리됨.");
            }
          } catch (Exception e) {
            log.error("QnaQueueListener - autoMatch :: 문의 자동 매칭 중 오류 발생 :", e);
          }
        }
      }
    });

    qnaAutoMatchThread.setDaemon(true); // 백그라운드 스레드
    qnaAutoMatchThread.start();
  }

  @Override
  public void destroy() {
    scheduler.shutdown(); // 애플리케이션 종료 시 리소스 정리
  }
}
