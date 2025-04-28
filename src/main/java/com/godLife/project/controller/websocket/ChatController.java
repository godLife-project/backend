package com.godLife.project.controller.websocket;

import com.godLife.project.dto.error.CustomWsErrorDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.MatchedListMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.QnaDetailMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;
import com.godLife.project.dto.serviceAdmin.AdminIdxAndIdDTO;
import com.godLife.project.dto.test.TestChatDTO;
import com.godLife.project.enums.MessageStatus;
import com.godLife.project.enums.WSDestination;
import com.godLife.project.exception.CustomException;
import com.godLife.project.exception.UnauthorizedException;
import com.godLife.project.exception.WebSocketBusinessException;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.impl.websocketImpl.WebSocketMessageService;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import com.godLife.project.service.interfaces.QnaMatchService;
import com.godLife.project.service.interfaces.QnaService;
import com.godLife.project.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final QnaService qnaService;

  private final GlobalExceptionHandler handler;

  private final WebSocketMessageService messageService;

  private final QnaMatchService matchService;

  private final ServiceAdminService serviceAdminService;

  private final RedisService redisService;

  private final UserService userService;

  private static final String QNA_WATCHER = "qna-watcher-";


  // 채팅 기능
  @MessageMapping("/roomChat/{roomNo}")
  @SendTo("/sub/roomChat/{roomNo}")
  public TestChatDTO broadcasting(final TestChatDTO request,
                                  @DestinationVariable(value = "roomNo") final String chatRoomNo) {
    request.setRoomNo(chatRoomNo);
    log.info("{roomNo : {}, request : {}}", chatRoomNo, request);

    return request;
  }

  /// 구독중인 유저 전체에게
  // 대기중인 루틴 리스트 전송
  @MessageMapping("/get/waitList/init")
  @SendTo("/sub/waitList")
  public WaitListMessageDTO broadcastWaitList(Principal principal) {
    return qnaService.getlistAllWaitQna(MessageStatus.RELOAD.getStatus(), principal.getName());
  }

  /// 구독중인 유저 한명에게
  // 할당 된 루틴 리스트 전송
  @MessageMapping("/get/matched/qna/init")
  public void sendMatchedQnaList(@Header("Authorization") String authHeader,
                                 Principal principal) {
    try {
      int adminIdx = handler.getUserIdxFromToken(authHeader);

      MatchedListMessageDTO request = qnaService.getlistAllMatchedQna(adminIdx, MessageStatus.RELOAD.getStatus(), principal.getName());
      //System.out.println(request);
      // 사용자 식별 ID를 얻는 방법: principal.getName() → WebSocket 인증된 사용자명
      messageService.sendToUser(principal.getName(), WSDestination.SUB_GET_MATCHED_QNA_LIST.getDestination(), request);
    } catch (Exception e) {
      throw new WebSocketBusinessException(e.getMessage(), 4001, principal.getName());
    }

  }

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
        MatchedListMessageDTO matchedQnA = qnaService.getMatchedSingleQna(adminInfo.getUserIdx(), qnaIdx, MessageStatus.ADD.getStatus(), principal.getName());
        // 관리자에게 매칭 리스트 업데이트
        if (matchedQnA != null) {
          messageService.sendToUser(principal.getName(), WSDestination.SUB_GET_MATCHED_QNA_LIST.getDestination(), matchedQnA);
          log.info("ChatController - takeWaitQna :: 문의 담당자에게 매칭되었습니다. ::> 매칭된 문의 - {} / 담당자 - {}", qnaIdx ,adminInfo);

          messageService.sendToAll(WSDestination.SUB_GET_WAIT_QNA_LIST.getDestination(), waitQna);
        }

        String info = "해당 문의가 할당 됐습니다.";
        messageService.sendToUser(principal.getName(), WSDestination.SUB_QNA_MATCH_RESULT.getDestination(), info);
      } else {
        String info = "다른 관리자에게 할당을 시도 중 이므로 수동 할당이 거부 됐습니다.";
        messageService.sendToUser(principal.getName(), WSDestination.SUB_QNA_MATCH_RESULT.getDestination(), info);
      }
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
      MatchedListMessageDTO matchedResponse = qnaService.getMatchedSingleQna(adminIdx, qnaIdx, MessageStatus.UPDATE.getStatus(), principal.getName());

      messageService.sendToUser(principal.getName(), WSDestination.SUB_GET_MATCHED_QNA_LIST.getDestination(), matchedResponse);
      messageService.sendToUser(principal.getName(), WSDestination.SUB_GET_QNA_DETAIL.getDestination() + qnaIdx, detailResponse);

      redisService.saveStringData(QNA_WATCHER + principal.getName(), String.valueOf(qnaIdx), 'h', 2);
    } catch (CustomException e) {
      if (e.getStatus() == HttpStatus.NOT_FOUND) {
        throw new WebSocketBusinessException(e.getMessage(), 4004, principal.getName());
      }
      if (e.getStatus() == HttpStatus.FORBIDDEN) {
        throw new WebSocketBusinessException(e.getMessage(), 4003, principal.getName());
      }
      throw new WebSocketBusinessException(e.getMessage(), 5000, principal.getName());
    } catch (Exception e) {
      throw new WebSocketBusinessException(e.getMessage(), 4001, principal.getName());
    }
  }

  // 상세 보기 닫음
  @MessageMapping("/close/detail")
  public void detailClose(Principal principal) {
    redisService.deleteData(QNA_WATCHER + principal.getName());
  }

  // 에러 메시지 처리
  @MessageExceptionHandler(WebSocketBusinessException.class)
  public void handleBusinessException(WebSocketBusinessException e) {
    System.out.println("동작함");
    messageService.sendToUser(e.getUsername(), "/queue/admin/errors", new CustomWsErrorDTO(e.getMessage(), e.getCode()));
  }




}
