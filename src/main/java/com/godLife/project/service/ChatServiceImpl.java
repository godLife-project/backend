package com.godLife.project.service;

import com.godLife.project.dto.chat.ChatRoom;
import com.godLife.project.exception.ChatRoomNotFoundException;
import com.godLife.project.service.interfaces.ChatService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatServiceImpl implements ChatService {

  // 메모리 기반 채팅방 저장소
  private final Map<String, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();

  @Override
  public ChatRoom createRoom(String roomId, String name) {
    ChatRoom room = ChatRoom.create(roomId, name);
    chatRoomMap.put(roomId, room);
    return room;
  }

  @Override
  public ChatRoom findRoomById(String roomId) {
    ChatRoom room = chatRoomMap.get(roomId);
    if (room == null) {
      throw new ChatRoomNotFoundException("존재하지 않는 채팅방입니다: " + roomId);
    }
    return room;
  }

  @Override
  public Collection<ChatRoom> findAllRooms() {
    return chatRoomMap.values();
  }

  @Override
  public boolean existsRoom(String roomId) {
    return chatRoomMap.containsKey(roomId);
  }

  // 사용자 퇴장 처리 (1:1 구조에 맞게 리팩토링)
  @Override
  public boolean exitRoom(String roomId, String username) {
    ChatRoom room = chatRoomMap.get(roomId);
    if (room == null) return false;

    // 사용자 또는 관리자인지 판단 후 제거
    if (username.equals(room.getUserName())) {
      room.setUserName(null);
      room.setUserSession(null);
    } else if (username.equals(room.getAdminName())) {
      room.setAdminName(null);
      room.setAdminSession(null);
    }

    // 두 명 다 나갔으면 방 삭제
    if (room.getUserSession() == null && room.getAdminSession() == null) {
      chatRoomMap.remove(roomId);
    }

    return true;
  }
}

