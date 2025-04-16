package com.godLife.project.controller.websocket;

import com.godLife.project.dto.test.TestChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final SimpMessagingTemplate template;

  @MessageMapping("/chat/{roomNo}")
  @SendTo("/sub/chat/{roomNo}")
  public TestChatDTO broadcasting(final TestChatDTO request,
                                  @DestinationVariable(value = "roomNo") final String chatRoomNo) {
    log.info("{roomNo : {}, request : {}}", chatRoomNo, request);
    request.setRoomNo(chatRoomNo);

    return request;
  }


}
