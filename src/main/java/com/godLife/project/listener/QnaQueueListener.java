package com.godLife.project.listener;

import com.godLife.project.dto.qnaWebsocket.listMessage.MatchedListMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;
import com.godLife.project.dto.serviceAdmin.AdminIdxAndIdDTO;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminInfos;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminList;
import com.godLife.project.enums.MessageStatus;
import com.godLife.project.enums.QnaStatus;
import com.godLife.project.enums.WSDestination;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.impl.websocketImpl.WebSocketMessageService;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import com.godLife.project.service.interfaces.QnaMatchService;
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
  private final ServiceAdminService serviceAdminService;
  private final WebSocketMessageService messageService;
  private final QnaService qnaService;
  private final QnaMatchService matchService;

  private static final String QNA_QUEUE_KEY = "qna_queue";
  private static final Object lock = new Object();

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  private Thread qnaAutoMatchThread;

  private volatile boolean running = true; // ✅ 1. 종료용 플래그

  private int restartCount = 0;
  private int waitCount = 0;
  private boolean lockPlag = false;

  public void wakeUp(int userIdx, String username) {
    if (lockPlag) {
      synchronized (lock) {
        restartCount = 0;
        waitCount = 0;
        lockPlag = false;
        lock.notify();
        log.info("🔔 QnaQueueListener - wakeUp :: 외부 요청에 의해 스레드를 깨웠습니다. [관리자ID = {} / 관리자IDX = {}]", username, userIdx);
      }
    }
  }

  @Override
  public void afterPropertiesSet() {
    qnaAutoMatchThread = new Thread(() -> {
      synchronized (lock) {
        while (running) { // ✅ 2. 플래그 기반 실행
          try {
            if (restartCount >= 3) {
              log.info("QnaQueueListener - autoMatch :: 장시간 새로운 문의가 없어 10분 대기합니다.");
              scheduler.schedule(() -> {
                synchronized (lock) {
                  lockPlag = false;
                  lock.notify();
                }
              }, 10, TimeUnit.MINUTES);

              lockPlag = true;
              lock.wait(); // 잠시 대기

              restartCount = 0;
              continue;
            }

            String result = redisService.brPopFromRedisQueue(QNA_QUEUE_KEY, 0);

            if (result != null) {
              int qnaIdx = Integer.parseInt(result);
              AdminIdxAndIdDTO adminInfo = matchService.getAdminInfo();

              if (adminInfo != null) {
                boolean isMatched = matchService.matchSingleQna(qnaIdx, adminInfo.getUserIdx());
                if (!isMatched) continue;

                serviceAdminService.refreshMatchCount(adminInfo.getUserIdx());

                List<String> notStatus = List.of(QnaStatus.WAIT.getStatus(), QnaStatus.COMPLETE.getStatus(), QnaStatus.DELETED.getStatus());

                MatchedListMessageDTO matchedQnA = qnaService.getMatchedSingleQna(
                    adminInfo.getUserIdx(), qnaIdx, MessageStatus.ADD.getStatus(), notStatus, adminInfo.getUserId());

                WaitListMessageDTO waitQna = matchService.setWaitListForMessage(qnaIdx, MessageStatus.REMOVE.getStatus());

                if (matchedQnA != null) {
                  messageService.sendToUser(adminInfo.getUserId(), WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), matchedQnA);
                  messageService.sendToAll(WSDestination.ALL_WAIT_QNA_LIST.getDestination(), waitQna);

                  List<ServiceCenterAdminInfos> accessAdminInfos = serviceAdminService.getAllAccessServiceAdminList();
                  List<ServiceCenterAdminList> accessAdminList = serviceAdminService.getAccessAdminListForMessage(accessAdminInfos);
                  messageService.sendToAll(WSDestination.ALL_ACCESS_ADMIN_LIST.getDestination(), accessAdminList);

                  log.info("QnaQueueListener - autoMatch :: 매칭 완료 → 문의번호: {}, 관리자: {}", qnaIdx, adminInfo);
                }

                Thread.sleep(500); // ✅ 4. 인터럽트 시 InterruptedException 발생 가능
              } else {
                if (waitCount % 6 == 0) {
                  log.info("QnaQueueListener - autoMatch :: 매칭 가능한 관리자가 없습니다. (5분 대기)");
                }
                redisService.rightPushToRedisQueue(QNA_QUEUE_KEY, String.valueOf(qnaIdx));
                waitCount++;

                scheduler.schedule(() -> {
                  synchronized (lock) {
                    lockPlag = false;
                    lock.notify();
                  }
                }, 5, TimeUnit.MINUTES);

                lockPlag = true;
                lock.wait();
              }
            }
          } catch (QueryTimeoutException e) {
            log.info("QnaQueueListener - autoMatch :: 1분간 새 문의 없음 → 10초 후 재시도");
            try {
              scheduler.schedule(() -> {
                synchronized (lock) {
                  lockPlag = false;
                  lock.notify();
                }
              }, 10, TimeUnit.SECONDS);

              lockPlag = true;
              lock.wait();
              restartCount++;
            } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
              log.info("QnaQueueListener - autoMatch :: 인터럽트 발생 → 안전 종료 중...");
              break; // 플래그로도 종료하지만, 인터럽트까지 왔다면 바로 탈출
            }
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("QnaQueueListener - autoMatch :: 스레드 인터럽트됨 → 종료 처리");
            break;
          } catch (Exception e) {
            log.error("QnaQueueListener - autoMatch :: 예외 발생", e);
          }
        }
      }
    });

    qnaAutoMatchThread.setDaemon(true);
    qnaAutoMatchThread.start();
  }

  @Override
  public void destroy() {
    log.info("QnaQueueListener - destroy :: 스레드 종료 시작");

    running = false; // ✅ 3. 루프 종료 플래그 변경

    synchronized (lock) {
      lockPlag = false;
      lock.notify(); // ✅ 3. 대기 중인 스레드 깨우기
    }

    if (qnaAutoMatchThread != null && qnaAutoMatchThread.isAlive()) {
      qnaAutoMatchThread.interrupt(); // ✅ 4. sleep() 중이라면 강제 종료 유도
    }

    scheduler.shutdownNow(); // 리소스 해제
  }
}
