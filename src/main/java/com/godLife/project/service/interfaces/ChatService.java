package com.godLife.project.service.interfaces;

import com.godLife.project.dto.chat.ChatRoom;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ChatService {
  ChatRoom createRoom(String roomId, String name);
  ChatRoom findRoomById(String roomId);
  Collection<ChatRoom> findAllRooms();
  boolean existsRoom(String roomId);
  boolean exitRoom(String roomId, String username);
}
