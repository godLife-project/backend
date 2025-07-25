package com.godLife.project.config.websocket;

import com.godLife.project.handler.websocket.StompExceptionHandler;
import com.godLife.project.handler.websocket.StompHandler;
import com.godLife.project.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketStompBrokerConfig implements WebSocketMessageBrokerConfigurer {

  private final StompHandler stompHandler;
  private final StompExceptionHandler stompExceptionHandler;

  @Value("${cors.allowed-origins}")
  private String allowedOrigins;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    /// 구독 - 클라이언트 측에서 /sub 으로 시작하는 엔드포인트로 구독 시 메시지 수신 가능
    config.enableSimpleBroker("/sub", "/queue");
    /// @MessageMapping 이 달린 메서드로 /pub 접두사로 사용 시 라우팅됨
    config.setApplicationDestinationPrefixes("/pub");
    config.setUserDestinationPrefix("/user");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {

    String[] origins = allowedOrigins.split(",\\s*");

    registry
        /// exception 처리 핸들러
        .setErrorHandler(stompExceptionHandler)
        /// 클라이언트가 WebSocket에 연결하기 위한 엔드포인트를 "/ws-stomp"로 설정
        .addEndpoint("/ws-stomp")
        /// 클라이언트의 origin을 명시적으로 지정
        .setAllowedOriginPatterns(origins)
        /// WebSocket을 지원하지 않는 브라우저에서도 SockJS를 통해 WebSocket 기능을 사용할 수 있게 합니다.
        .withSockJS();
  }

  /// jwt 토큰 검증 후 이상할 경우 exception 터트림
  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompHandler);
  }
}
