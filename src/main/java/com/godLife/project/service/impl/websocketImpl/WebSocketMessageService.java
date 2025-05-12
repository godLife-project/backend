package com.godLife.project.service.impl.websocketImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketMessageService {

  private final SimpMessagingTemplate messagingTemplate;

  /**
   * 특정 유저에게 큐 기반 메시지를 전송합니다.
   *
   * @param username       메시지를 받을 유저 이름 (principal.getName()에 해당)
   * @param destination    구독 경로 (예: "/queue/qna")
   * @param payload        보낼 메시지 내용 (DTO, String 등 직렬화 가능한 객체)
   */
  public void sendToUser(String username, String destination, Object payload) {
    messagingTemplate.convertAndSendToUser(username, destination, payload);
  }

  /**
   * 전체 유저에게 메시지를 브로드캐스팅할 수 있는 함수 (선택사항)
   *
   * @param destination    전체 유저가 구독하는 경로 (예: "/topic/qna")
   * @param payload        보낼 메시지 내용
   */
  public void sendToAll(String destination, Object payload) {
    messagingTemplate.convertAndSend(destination, payload);
  }
}
