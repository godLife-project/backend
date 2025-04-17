package com.godLife.project.controller.websocket;

import com.godLife.project.dto.error.CustomWsErrorDTO;
import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import com.godLife.project.dto.qnaWebsocket.WaitListMessageDTO;
import com.godLife.project.dto.test.TestChatDTO;
import com.godLife.project.exception.WebSocketBusinessException;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.interfaces.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final QnaService qnaService;

  private final GlobalExceptionHandler handler;

  private final SimpMessagingTemplate messagingTemplate;


  // 채팅 기능
  @MessageMapping("/chat/{roomNo}")
  @SendTo("/sub/chat/{roomNo}")
  public TestChatDTO broadcasting(final TestChatDTO request,
                                  @DestinationVariable(value = "roomNo") final String chatRoomNo) {
    request.setRoomNo(chatRoomNo);
    log.info("{roomNo : {}, request : {}}", chatRoomNo, request);

    return request;
  }

  /// 구독중인 유저 전체에게
  // 대기중인 루틴 리스트 전송
  @MessageMapping("/get/waitList/{roomNo}")
  @SendTo("/sub/waitList/{roomNo}")
  public WaitListMessageDTO broadcastWaitList(@DestinationVariable(value = "roomNo") final String chatRoomNo) {
    return qnaService.getlistAllWaitQna(chatRoomNo);
  }

//  /// 구독중인 유저 중 데이터를 요청한 단일 유저에게
//  // 매칭된 루틴 리스트 전송
//  @MessageMapping("/get/matched/qna")
//  @SendToUser("/queue/qna")
//  public List<QnaMatchedListDTO> test(@Header("Authorization") String authHeader) {
//    int adminIdx = handler.getUserIdxFromToken(authHeader);
//    return  qnaService.getlistAllMatchedQna(adminIdx);
//  }

  @MessageMapping("/get/matched/qna")
  public void sendMatchedQnaList(@Header("Authorization") String authHeader,
                                 Principal principal) {
    int adminIdx = handler.getUserIdxFromToken(authHeader);
    List<QnaMatchedListDTO> matchedList = qnaService.getlistAllMatchedQna(adminIdx);

    // 사용자 식별 ID를 얻는 방법: principal.getName() → WebSocket 인증된 사용자명
    messagingTemplate.convertAndSendToUser(
        principal.getName(),            // 유저 이름 (username)
        "/queue/qna",                   // 구독 경로
        matchedList                     // 전송할 데이터
    );
  }

  // 에러 메시지 처리
  @MessageExceptionHandler(WebSocketBusinessException.class)
  @SendToUser("/queue/errors")
  public CustomWsErrorDTO handleBusinessException(WebSocketBusinessException e) {
    return new CustomWsErrorDTO(e.getMessage(), e.getCode());
  }




}
