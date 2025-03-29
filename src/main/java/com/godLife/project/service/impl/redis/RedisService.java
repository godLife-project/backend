package com.godLife.project.service.impl.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  // 데이터 저장
  public void saveData(String key, String value, long timeoutSeconds) {
    redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutSeconds));
  }

  // 데이터 조회
  public String getData(String key) {
    return (String) redisTemplate.opsForValue().get(key);
  }

  // 데이터 삭제
  public void deleteData(String key) {
    redisTemplate.delete(key);
  }

  // 키 존재 여부 확인
  public boolean checkExistsValue(String key) {
    return redisTemplate.hasKey(key);
  }
}
