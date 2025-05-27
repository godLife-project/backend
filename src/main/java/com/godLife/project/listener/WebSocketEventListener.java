package com.godLife.project.listener;

import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminInfos;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminList;
import com.godLife.project.enums.WSDestination;
import com.godLife.project.handler.redisSession.RedisSessionManager;
import com.godLife.project.jwt.JWTUtil;
import com.godLife.project.mapper.VerifyMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.impl.websocketImpl.WebSocketMessageService;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

  private final JWTUtil jwtUtil;

  private final RedisSessionManager redisSessionManager;

  private final RedisService redisService;

  private final ServiceAdminService serviceAdminService;

  private final WebSocketMessageService messageService;

  private final VerifyMapper verifyMapper;

  private static final String SAVE_SERVICE_ADMIN_STATUS = "save-service-admin-status:";
  private static final String IS_LOGOUT_ADMIN = "admin-is-logout:";

  // 클라이언트가 연결되었을 때 세션 정보 저장
  @EventListener
  public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    // CONNECT_ACK에서 Principal 정보는 직접적으로 안 들어있음 → simpConnectMessage에서 꺼내야 함
    Message<?> connectMessage = (Message<?>) headerAccessor.getHeader("simpConnectMessage");
    StompHeaderAccessor connectAccessor = StompHeaderAccessor.wrap(connectMessage);

    List<String> authHeaders = connectAccessor.getNativeHeader("Authorization");
    String token = (authHeaders != null && !authHeaders.isEmpty()) ? authHeaders.get(0).replace("Bearer ", "") : null;

    String userName = null;
    if (token != null) {
      userName = jwtUtil.getUsername(token);
      String roleStr = Optional.ofNullable(jwtUtil.getRole(token)).orElse("1");

      int role = Integer.parseInt(roleStr);

      if (role >= 2) {
        List<Integer> validAuthList = Arrays.asList(3, 4, 6, 7);
        if (validAuthList.contains(role)) {
          log.info("고객서비스 접근 권한 확인..고객센터 테이블에 데이터를 저장합니다.");
          serviceAdminService.setCenterLoginByAdmin3467(userName);  // 권한이 3,4,6,7 이면 고객센터 로그인 처리

        }
      }
    }
    String sessionId = connectAccessor.getSessionId();

    // 세션 ID와 유저 정보 저장
    redisSessionManager.addSession(userName, sessionId);
    log.info("유저 : {} 연결됨, 세션 ID : {}", userName, sessionId);
  }

  // 클라이언트가 연결을 끊었을 때 세션 정보 제거
  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String sessionId = headerAccessor.getSessionId();

    // 세션 ID를 기준으로 해당 유저 정보 제거
    String userName = redisSessionManager.getUsernameBySessionId(sessionId);

    if (userName != null) {
      redisSessionManager.removeSession(userName);

      int adminIdx = verifyMapper.getUserIdxByUserId(userName);

      // 클라이언트에게 관리자 목록 조회
      List<ServiceCenterAdminInfos> accessAdminInfos = serviceAdminService.getAllAccessServiceAdminList();

      for (ServiceCenterAdminInfos temp : accessAdminInfos) {
        int status = temp.getStatus();
        int userIdx = temp.getUserIdx();
        // 활성화 상태 관리자의 정보 임시 저장
        String isLogout = redisService.getStringData(IS_LOGOUT_ADMIN + userIdx);
        if (status == 1 && isLogout == null) {
          redisService.saveStringData(SAVE_SERVICE_ADMIN_STATUS + userIdx, String.valueOf(status), 'h', 2);
        }
      }
      // 연결 해제 된 관리자 데이터 제거
      serviceAdminService.disconnectAdminServiceCenter(userName);
      // 관리자 목록에서 삭제한 관리자 명단 제거
      accessAdminInfos.removeIf(admin -> admin.getUserIdx() == adminIdx);

      List<ServiceCenterAdminList> accessAdminList = new ArrayList<>();
      for (ServiceCenterAdminInfos info : accessAdminInfos) {
        accessAdminList.add(new ServiceCenterAdminInfos(info));
      }
      messageService.sendToAll(WSDestination.ALL_ACCESS_ADMIN_LIST.getDestination(), accessAdminList);

      log.info("유저 : {} 연결 끊어짐", userName);
    }
  }
}
