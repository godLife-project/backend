package com.godLife.project.dto.request.adminChat;

import lombok.Data;

import java.util.List;

/**
 * <Strong>채팅방 생성 시 받아 올 데이터 DTO</Strong>
 * <hr/>
 * <p>{@code (String) name} 채팅방 제목, null 값이 들어올 경우 자동 생성</p>
 * <p>{@code (boolean) publicRoom} 채팅방 공개 여부, 기본값 : 0 == 비공개</p>
 * <p>{@code (int) createdBy} 작성자 인덱스 번호</p>
 * <p>{@code (List<Integer>) roleIndexes} 채팅방 입장 가능 권한 설정, 공개 방에만 적용</p>
 * <p>{@code (List<String>) invites} 초기 멤버 초대, 방 생성 시 멤버 바로 초대 할 때 사용</p>
 * <hr/>
 */
@Data
public class ChatCreateDTO {
  // 채팅방 제목
  private String name;
  // 공개 채팅방 여부
  private boolean publicRoom;
  // 채팅방 개설자
  private int createdBy;
  // 공개 채팅방 입장 제한 권한
  private List<Integer> roleIndexes;
  // 채팅방 생성 시 초기 멤버 초대
  private List<String> invites;
}
