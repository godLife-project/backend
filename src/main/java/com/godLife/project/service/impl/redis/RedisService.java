package com.godLife.project.service.impl.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RedisService {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  // 데이터 저장
  public void saveStringData(String key, String value, char type, long timeout) {

    Duration duration = switch (Character.toLowerCase(type)) {
      case 's' -> Duration.ofSeconds(timeout);
      case 'm' -> Duration.ofMinutes(timeout);
      case 'h' -> Duration.ofHours(timeout);
      default -> null;
    };

    if (duration != null) {
      redisTemplate.opsForValue().set(key, value, duration);
    } else {
      redisTemplate.opsForValue().set(key, value);
    }
  }

  // 리스트 DTO 저장
  public <T> void saveListData(String key, List<T> dataList, char type, long timeout) {
    try {
      String json = objectMapper.writeValueAsString(dataList); // 직렬화

      Duration duration = switch (Character.toLowerCase(type)) {
        case 's' -> Duration.ofSeconds(timeout);
        case 'm' -> Duration.ofMinutes(timeout);
        case 'h' -> Duration.ofHours(timeout);
        default -> null;
      };

      if (duration != null) {
        redisTemplate.opsForValue().set(key, json, duration);
      } else {
        redisTemplate.opsForValue().set(key, json);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException("리스트 직렬화 오류", e);
    }
  }

  // 데이터 조회
  public String getStringData(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  // 리스트 DTO 데이터 조회
  public <T> List<T> getListData(String key, Class<T> clazz) {
    String json = redisTemplate.opsForValue().get(key);
    if (json == null) return null;

    try {
      JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
      return objectMapper.readValue(json, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("리스트 역직렬화 오류", e);
    }
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
