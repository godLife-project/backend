package com.godLife.project.listener;

import com.godLife.project.enums.QnaRedisKey;
import com.godLife.project.enums.QnaStatus;
import com.godLife.project.service.interfaces.QnaService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class RedisEventListener implements MessageListener {

  private final RedisMessageListenerContainer redisMessageListenerContainer;
  private final QnaService qnaService;

  // 수동으로 리스너 등록
  @PostConstruct
  public void init() {
    // expired :: 키가 만료되어 삭제되는 경우에만 리스너 동작
    Topic topic = new PatternTopic("__keyevent@0__:expired"); // DB 0번 기준
    redisMessageListenerContainer.addMessageListener(this, topic);
  }

  @Override
  public void onMessage(@NonNull Message message, byte[] pattern) {

    // 어떤 이벤트 인지 확인
    String eventPattern = new String(pattern);
    String eventType = eventPattern.substring(eventPattern.lastIndexOf(":") + 1); // "expired"

    // 특정 이벤트에 대한 로직 분기
    // 특정 채널에 대한 로직을 구현하려면 eventPattern에서 채널별로 분리하는 로직 구현할 것.
    switch (eventType) {
      case "expired":
        // 만료 처리
        String expiredKey = message.toString();
        if (expiredKey.contains(QnaRedisKey.QNA_ADMIN_ANSWERED.getKey())) {
          System.out.println("만료된 키 : " + expiredKey);
          int qnaIdx = Integer.parseInt(expiredKey.substring(expiredKey.lastIndexOf(":") + 1));
          //System.out.println("문의 인덱스 : " + qnaIdx);

          qnaService.setQnaStatus(qnaIdx, null, QnaStatus.SLEEP.getStatus(), Collections.singletonList(QnaStatus.RESPONDING.getStatus()));
        }

        break;
      case "del":
        // 삭제 처리
        break;
      case "set":
        // set 처리
        break;
      default:
        // 기타
        break;
    }


  }
}
