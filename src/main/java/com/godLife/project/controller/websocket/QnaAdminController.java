package com.godLife.project.controller.websocket;

import com.godLife.project.dto.error.CustomWsErrorDTO;
import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.MatchedListMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.QnaDetailMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;
import com.godLife.project.dto.serviceAdmin.AdminIdxAndIdDTO;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminInfos;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminList;
import com.godLife.project.dto.statistics.response.ResponseQnaAdminStat;
import com.godLife.project.enums.MessageStatus;
import com.godLife.project.enums.QnaRedisKey;
import com.godLife.project.enums.QnaStatus;
import com.godLife.project.enums.WSDestination;
import com.godLife.project.exception.CustomException;
import com.godLife.project.exception.WebSocketBusinessException;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.impl.websocketImpl.WebSocketMessageService;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import com.godLife.project.service.interfaces.QnaMatchService;
import com.godLife.project.service.interfaces.QnaService;
import com.godLife.project.service.interfaces.statistics.ServiceAdminStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QnaAdminController {

  private final QnaService qnaService;

  private final GlobalExceptionHandler handler;

  private final WebSocketMessageService messageService;

  private final QnaMatchService matchService;

  private final ServiceAdminService serviceAdminService;

  private final ServiceAdminStatService adminStatService;

  private final RedisService redisService;

  private static final String SAVE_SERVICE_ADMIN_STATUS = "save-service-admin-status:";


  /// 구독중인 유저 전체에게
  // 대기중인 루틴 리스트 전송
  @MessageMapping("/get/waitList/init")
  @SendTo("/sub/waitList")
  public WaitListMessageDTO broadcastWaitList(Principal principal) {
    return qnaService.getlistAllWaitQna(MessageStatus.RELOAD.getStatus(), principal.getName());
  }

  // 1:1 문의 관리 페이지 접속 상담원 목록 조회
  @MessageMapping("/get/access/serviceCenter")
  @SendTo("/sub/access/list")
  public List<ServiceCenterAdminList> broadcastAccessList() {
    List<ServiceCenterAdminInfos> adminInfos = serviceAdminService.getAllAccessServiceAdminList();

    for (int i = 0; i < adminInfos.size(); i++) {
      ServiceCenterAdminInfos temp = adminInfos.get(i);
      int userIdx = temp.getUserIdx();

      String isSaved = redisService.getStringData(SAVE_SERVICE_ADMIN_STATUS + userIdx);
      if (isSaved != null) {
        // 해당 유저 상태 전환
        serviceAdminService.setAdminStatusTrue(userIdx);

        // 상태 변경 후 리스트 업데이트
        temp.setStatus(1); // 상태 변경
        adminInfos.set(i, temp); // 수정된 객체를 리스트에 다시 넣기
      }
    }

    List<ServiceCenterAdminList> adminList = new ArrayList<>();
    for (ServiceCenterAdminInfos info : adminInfos) {
      adminList.add(new ServiceCenterAdminInfos(info));
    }
    return adminList;
  }


  /// 구독중인 유저 한명에게
  // 할당 된 루틴 리스트 전송
  @MessageMapping("/get/matched/qna/init")
  public void sendMatchedQnaList(@Header("Authorization") String authHeader,
                                 Principal principal) {
    try {
      int adminIdx = handler.getUserIdxFromToken(authHeader);

      List<String> notStatus = new ArrayList<>();
      notStatus.add(QnaStatus.WAIT.getStatus());
      notStatus.add(QnaStatus.COMPLETE.getStatus());
      notStatus.add(QnaStatus.DELETED.getStatus());

      MatchedListMessageDTO request = qnaService.getlistAllQnaByFindNotStatus(adminIdx, MessageStatus.RELOAD.getStatus(), notStatus, principal.getName());
      //System.out.println(request);
      // 사용자 식별 ID를 얻는 방법: principal.getName() → WebSocket 인증된 사용자명
      messageService.sendToUser(principal.getName(), WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), request);
    } catch (Exception e) {
      throw new WebSocketBusinessException(e.getMessage(), 4001, principal.getName());
    }

  }

  // 문의 수동 할당
  @MessageMapping("/take/waitQna/{qnaIdx}")
  public void takeWaitQna(@Header("Authorization") String authHeader,
                          @DestinationVariable(value = "qnaIdx") final int qnaIdx,
                          Principal principal) {
    try {
      AdminIdxAndIdDTO adminInfo = new AdminIdxAndIdDTO();
      adminInfo.setUserIdx(handler.getUserIdxFromToken(authHeader));
      adminInfo.setUserId(handler.getUserNameFromToken(authHeader));

      if (qnaIdx == 0) {
        throw new WebSocketBusinessException("문의를 선택해 주세요. 해당 문의가 삭제 됐거나, null 혹은 0 으로 요청했습니다.", 4004, principal.getName());
      }
      if (adminInfo.getUserIdx() == 0) {
        throw new WebSocketBusinessException("유효하지 않은 jwt 토큰 이거나, 존재하지 않은 관리자 입니다.", 4003, principal.getName());
      }

      // 매칭 시도
      boolean isMatched = matchService.matchSingleQna(qnaIdx, adminInfo.getUserIdx());

      if (isMatched) {
        // 관리자 문의 수 최신화
        serviceAdminService.refreshMatchCount(adminInfo.getUserIdx());

        // 모든 관리자에게 대기중 문의 리스트 중 매칭된 문의는 삭제
        WaitListMessageDTO waitQna =  matchService.setWaitListForMessage(qnaIdx, MessageStatus.REMOVE.getStatus());
        // 매칭 된 관리자에게 매칭 문의 리스트 추가
        List<String> notStatus = new ArrayList<>();
        notStatus.add(QnaStatus.WAIT.getStatus());
        notStatus.add(QnaStatus.COMPLETE.getStatus());
        notStatus.add(QnaStatus.DELETED.getStatus());
        MatchedListMessageDTO matchedQnA = qnaService.getMatchedSingleQna(adminInfo.getUserIdx(), qnaIdx, MessageStatus.ADD.getStatus(), notStatus, principal.getName());
        // 관리자에게 매칭 리스트 업데이트
        if (matchedQnA != null) {
          messageService.sendToUser(principal.getName(), WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), matchedQnA);
          log.info("ChatController - takeWaitQna :: 문의 담당자에게 매칭되었습니다. ::> 매칭된 문의 - {} / 담당자 - {}", qnaIdx ,adminInfo);

          messageService.sendToAll(WSDestination.ALL_WAIT_QNA_LIST.getDestination(), waitQna);
        }

        String infoString = "해당 문의가 할당 됐습니다.";
        messageService.sendToUser(principal.getName(), WSDestination.QUEUE_QNA_MATCH_RESULT.getDestination(), infoString);

        // 클라이언트에게 관리자 목록 전송
        List<ServiceCenterAdminInfos> accessAdminInfos = serviceAdminService.getAllAccessServiceAdminList();

        List<ServiceCenterAdminList> accessAdminList = new ArrayList<>();
        for (ServiceCenterAdminInfos info : accessAdminInfos) {
          accessAdminList.add(new ServiceCenterAdminInfos(info));
        }
        messageService.sendToAll(WSDestination.ALL_ACCESS_ADMIN_LIST.getDestination(), accessAdminList);
      } else {
        String info = "다른 관리자에게 할당을 시도 중 이므로 수동 할당이 거부 됐습니다.";
        messageService.sendToUser(principal.getName(), WSDestination.QUEUE_QNA_MATCH_RESULT.getDestination(), info);
      }
    } catch (WebSocketBusinessException e) {
      throw new WebSocketBusinessException(e.getMessage(), e.getCode(), principal.getName());
    } catch (Exception e) {
      throw new WebSocketBusinessException(e.getMessage(), 4001, principal.getName());
    }

  }

  // 문의 상세 보기
  @MessageMapping("/get/matched/qna/detail/{qnaIdx}")
  public void getMatchedQnaDetail(@Header("Authorization") String authHeader,
                                  @DestinationVariable(value = "qnaIdx") final int qnaIdx,
                                  Principal principal) {
    try {
      int adminIdx = handler.getUserIdxFromToken(authHeader);

      if (qnaIdx <= 0) {
        throw new WebSocketBusinessException("문의 인덱스를 선택해주세요. 문의 인덱스는 0 보다 커야 합니다.", 4000, principal.getName());
      }
      if (adminIdx == 0) {
        throw new WebSocketBusinessException("유효하지 않은 jwt 토큰이거나, 존재하지 않은 관리자 입니다.", 4003, principal.getName());
      }

      // 상세보기 데이터
      QnaDetailMessageDTO detailResponse = qnaService.getQnaDetails(qnaIdx, MessageStatus.RELOAD.getStatus(), adminIdx);
      // 해당 문의 정보 데이터
      List<String> notStatus = new ArrayList<>();
      notStatus.add(QnaStatus.WAIT.getStatus());
      //notStatus.add(QnaStatus.COMPLETE.getStatus());
      notStatus.add(QnaStatus.DELETED.getStatus());
      MatchedListMessageDTO matchedResponse = qnaService.getMatchedSingleQna(adminIdx, qnaIdx, MessageStatus.UPDATE.getStatus(), notStatus, principal.getName());

      List<QnaMatchedListDTO> tempMatchedQna = matchedResponse.getMatchedQnA();
      for (QnaMatchedListDTO tempQna : tempMatchedQna) {
        if (tempQna.getQnaStatus().equals(QnaStatus.COMPLETE.getStatus())) {
          messageService.sendToUser(principal.getName(), WSDestination.QUEUE_COMPLETE_QNA_LIST.getDestination(), matchedResponse);
        } else {
          messageService.sendToUser(principal.getName(), WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), matchedResponse);
        }
      }
      messageService.sendToUser(principal.getName(), WSDestination.QUEUE_QNA_DETAIL.getDestination() + qnaIdx, detailResponse);

      redisService.saveStringData(QnaRedisKey.QNA_WATCHER.getKey() + principal.getName(), String.valueOf(qnaIdx), 'h', 2);
    } catch (WebSocketBusinessException e) {
      throw new WebSocketBusinessException(e.getMessage(), e.getCode(), principal.getName());
    } catch (CustomException e) {
      if (e.getStatus() == HttpStatus.NOT_FOUND) {
        throw new WebSocketBusinessException(e.getMessage(), 4004, principal.getName());
      }
      if (e.getStatus() == HttpStatus.FORBIDDEN) {
        throw new WebSocketBusinessException(e.getMessage(), 4003, principal.getName());
      }
      throw new WebSocketBusinessException(e.getMessage(), 5000, principal.getName());
    } catch (Exception e) {
      throw new WebSocketBusinessException(e.getMessage(), 5000, principal.getName());
    }
  }

  // 상세 보기 닫음
  @MessageMapping("/close/detail")
  public void detailClose(Principal principal) {
    redisService.deleteData(QnaRedisKey.QNA_WATCHER.getKey() + principal.getName());
  }

  // 완료 처리된 문의 조회
  @MessageMapping("/get/complete/qnaList/init")
  public void getCompletedQnaList(Principal principal,
                                  @Header("Authorization") String authHeader) {
    try {
      int adminIdx = handler.getUserIdxFromToken(authHeader);

      List<String> notStatus = new ArrayList<>();
      notStatus.add(QnaStatus.WAIT.getStatus());
      notStatus.add(QnaStatus.CONNECT.getStatus());
      notStatus.add(QnaStatus.DELETED.getStatus());
      notStatus.add(QnaStatus.RESPONDING.getStatus());
      notStatus.add(QnaStatus.SLEEP.getStatus());

      MatchedListMessageDTO request = qnaService.getlistAllQnaByFindNotStatus(adminIdx, MessageStatus.RELOAD.getStatus(), notStatus, principal.getName());
      messageService.sendToUser(principal.getName(), WSDestination.QUEUE_COMPLETE_QNA_LIST.getDestination(), request);
    } catch (WebSocketBusinessException e) {
      throw new WebSocketBusinessException(e.getMessage(), e.getCode(), e.getUsername());
    } catch (Exception e) {
      throw new WebSocketBusinessException(e.getMessage(), 5000, principal.getName());
    }
  }

  // 접속 상담원 문의 처리 통계 조회
  @MessageMapping("/get/qna/statistics/init")
  public void getQnaAdminStats(Principal principal,
                               @Header("Authorization") String authHeader) {
    try {
      int adminIdx = handler.getUserIdxFromToken(authHeader);

      ResponseQnaAdminStat qnaAdminStat = adminStatService.getQnaAdminStats(adminIdx);

      messageService.sendToUser(principal.getName(), WSDestination.QUEUE_QNA_ADMIN_STATISTICS.getDestination(), qnaAdminStat);
    } catch (Exception e) {
      throw new WebSocketBusinessException(e.getMessage(), 5000, principal.getName());
    }

  }

  // 에러 메시지 처리
  @MessageExceptionHandler(WebSocketBusinessException.class)
  public void handleBusinessException(WebSocketBusinessException e) {
    System.out.println("동작함");
    messageService.sendToUser(e.getUsername(), "/queue/admin/errors", new CustomWsErrorDTO(e.getMessage(), e.getCode()));
  }




}
