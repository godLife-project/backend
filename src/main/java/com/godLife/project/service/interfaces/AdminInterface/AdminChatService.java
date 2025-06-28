package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.request.adminChat.ChatCreateDTO;

public interface AdminChatService {

  /**
   * <strong>채팅방 생성 로직</strong>
   * <p>{@code ChatCreateDTO} 를 통해 채팅방 설정 및 생성 합니다.</p>
   * <hr/>
   * <p>{@code name}, {@code publicRoom}, {@code createdBy} 를 통해 {@code ADMIN_CHAT_ROOMS} 테이블에 데이터 추가</p>
   * <p>{@code roleIndexes} 를 통해 {@code ADMIN_CHAT_ROOM_ROLES} 테이블에 권한 데이터 추가</p>
   * <p>{@code invites} 를 통해 {@code ADMIN_CHAT_ROOM_MEMBERS} 테이블에 초기 멤버 추가</p>
   * @param chatCreateDTO 톡방 생성 시 필요한 기본 설정 정보
   */
  int createAdminChatRoom(ChatCreateDTO chatCreateDTO);
}
