package com.godLife.project.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompBrokerConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    /// 구독 - 클라이언트 측에서 /sub 으로 시작하는 엔드포인트로 구독 시 메시지 수신 가능
    config.enableSimpleBroker("/sub");
    /// @MessageMapping 이 달린 메서드로 /pub 접두사로 사용 시 라우팅됨
    config.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
        /// 클라이언트가 WebSocket에 연결하기 위한 엔드포인트를 "/ws-stomp"로 설정
        .addEndpoint("/ws-stomp")
        /// 클라이언트의 origin을 명시적으로 지정
        .setAllowedOriginPatterns("*")
        /// WebSocket을 지원하지 않는 브라우저에서도 SockJS를 통해 WebSocket 기능을 사용할 수 있게 합니다.
        .withSockJS();
  }
}
