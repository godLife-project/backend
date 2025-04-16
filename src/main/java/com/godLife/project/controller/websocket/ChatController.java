package com.godLife.project.controller.websocket;

import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import com.godLife.project.dto.qnaWebsocket.WaitListMessageDTO;
import com.godLife.project.dto.test.TestChatDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final QnaMapper qnaMapper;

  private final GlobalExceptionHandler handler;

  @MessageMapping("/chat/{roomNo}")
  @SendTo("/sub/chat/{roomNo}")
  public TestChatDTO broadcasting(final TestChatDTO request,
                                  @DestinationVariable(value = "roomNo") final String chatRoomNo) {
    request.setRoomNo(chatRoomNo);
    log.info("{roomNo : {}, request : {}}", chatRoomNo, request);

    return request;
  }

  @MessageMapping("/get/waitList/{roomNo}")
  @SendTo("/sub/waitList/{roomNo}")
  public WaitListMessageDTO broadcastWaitList(@DestinationVariable(value = "roomNo") final String chatRoomNo) {
    List<QnaWaitListDTO> waitList = qnaMapper.getlistAllWaitQna();
    WaitListMessageDTO request = new WaitListMessageDTO();
    request.setWaitQnaInfos(waitList);
    request.setRoomNo(chatRoomNo);

    return request;
  }

  @MessageMapping("/get/matched/qna")
  @SendToUser("/queue/qna")
  public String test(@Header("Authorization") String authHeader) {


    return handler.getUserNameFromToken(authHeader) + " -- " + handler.getUserIdxFromToken(authHeader);
  }




}
