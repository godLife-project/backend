package com.godLife.project.handler.redisSession;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSessionManager {

  private final RedisTemplate<String, String> redisTemplate;

  private static final String USER_TO_SESSION_KEY = "websocket:user-to-session";
  private static final String SESSION_TO_USER_KEY = "websocket:session-to-user";

  public void addSession(String username, String sessionId) {
    redisTemplate.opsForHash().put(USER_TO_SESSION_KEY, username, sessionId);
    redisTemplate.opsForHash().put(SESSION_TO_USER_KEY, sessionId, username);
  }

  public String getUsernameBySessionId(String sessionId) {
    return (String) redisTemplate.opsForHash().get(SESSION_TO_USER_KEY, sessionId);
  }

  public void removeSession(String username) {
    String sessionId = (String) redisTemplate.opsForHash().get(USER_TO_SESSION_KEY, username);

    if (sessionId != null) {
      redisTemplate.opsForHash().delete(SESSION_TO_USER_KEY, sessionId);
    }
    redisTemplate.opsForHash().delete(USER_TO_SESSION_KEY, username);
  }

  public String getSessionIdByUsername(String username) {
    return (String) redisTemplate.opsForHash().get(USER_TO_SESSION_KEY, username);
  }

}
