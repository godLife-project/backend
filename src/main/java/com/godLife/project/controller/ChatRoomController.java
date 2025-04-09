package com.godLife.project.controller;

import com.godLife.project.dto.chat.ChatRoom;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatService chatService;
  @Autowired
  private GlobalExceptionHandler handler;

  // 전체 채팅방 조회
  @GetMapping("/admin/rooms")
  public Collection<ChatRoom> getAllRooms() {
    return chatService.findAllRooms();
  }

  // 채팅방 생성
  @PostMapping("/room/me")
  public ChatRoom createPrivateRoom(@RequestHeader("Authorization") String authHeader) {
    String name = handler.getUserNameFromToken(authHeader);
    String roomId = "user_" + name;

    // 중복 방지: 이미 있으면 재사용
    if (chatService.existsRoom(roomId)) {
      return chatService.findRoomById(roomId);
    }

    return chatService.createRoom(roomId, name + "님의 1:1 문의방");
  }


  @GetMapping("/admin/room/{roomId}")
  public ResponseEntity<ChatRoom> getRoomDetail(@PathVariable String roomId) {
    ChatRoom room = chatService.findRoomById(roomId);
    if (room == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(room);
  }


  //  사용자 채팅 종료
  @PatchMapping("/room/{roomId}/exit")
  public ResponseEntity<Map<String, Object>> exitChatRoom(@PathVariable String roomId,
                                                          @RequestHeader("Authorization") String authHeader) {
    String username = handler.getUserNameFromToken(authHeader);
    boolean result = chatService.exitRoom(roomId, username);

    if (result) {
      return ResponseEntity.ok(handler.createResponse(200, "채팅 종료 완료"));
    } else {
      return ResponseEntity.status(404).body(handler.createResponse(404, "존재하지 않는 채팅방입니다."));
    }
  }
}
