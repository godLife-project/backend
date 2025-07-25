package com.godLife.project.dto.chat;

import lombok.Data;

/**
 * <strong>채팅방 입장 가능 권한 테이블 DTO</strong>
 * <hr/>
 * <p>{@code int roomIdx} 채팅방 고유 인덱스 번호</p>
 * <p>{@code int roleIdx} 채팅방 입장 가능 권한 인덱스 번호</p>
 * <hr/>
 * <b>권한은 중복 지정 할 수 없습니다.</b>
 * <p><small>(예시 : 권한 인덱스 = 1,2,3 ..> 가능)</small></p>
 * <small>(예시 : 권한 인덱스 = 1,1,2 ..> 불가능)</small>
 */
@Data
public class ChatRoles {
  // 채팅방 고유 인덱스 번호
  private int roomIdx;
  // 채팅방 입장 가능 권한 인덱스 번호
  private int	roleIdx;
}
