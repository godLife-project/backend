package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.chat.ChatMembers;
import com.godLife.project.dto.chat.ChatRoles;
import com.godLife.project.dto.chat.ChatRoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminChatMapper {

  /**
   * <strong>채팅방 생성 쿼리</strong>
   * <hr/>
   * <p>{@code name}, {@code isPublic}, {@code createdBy} 를 사용합니다.</p>
   * <p>{@code (String) name} 이 {@code null} 일 경우, '채팅방 이름 미정' 으로 저장됩니다.</p>
   * <p>{@code createdBy} 에 올바른 값을 지정하지 않을 경우, 에러가 발생합니다.</p>
   * @param chatRoomDTO 채팅방 생성을 위한 테이블 DTO
   */
  void createChatRoom(ChatRoomDTO chatRoomDTO);

  /**
   * <strong>공개 채팅방 생성 시 접근 가능 권한 설정 쿼리</strong>
   * <hr/>
   * <h3><채팅방 생성이 선행 되어야 합니다.></h3>
   * <p>{@code ChatRoles} DTO에 새로 생성한 채팅방의 {@code roomIdx} 를 세팅하고,</p>
   * <p>클라이언트가 지정한 권한 인덱스 번호를 {@code roleIdx} 에 세팅하여 전달해야 합니다.</p>
   * @param role {@code ChatRoles}
   */
  void setRolesToChatRoom(ChatRoles role);

  /**
   * <strong>채팅방 멤버 초대(참여) 쿼리 [다수]</strong>
   * <hr/>
   * <h3><채팅방 생성이 선행 되어야 합니다.></h3>
   * <h5>채팅방 초대 시</h5>
   * <p>{@code ChatMembers} DTO에 새로 생성한 채팅방의 {@code roomIdx} 를 세팅하고,</p>
   * <p>클라이언트가 초대한 유저 인덱스 번호를 {@code adminIdx} 에 세팅하여 {@code List} 에 담아 전달해야 합니다.</p>
   * <hr/>
   * @param invites {@code List<ChatMembers>}
   */
  void addChatMembersBatch(@Param("list") List<ChatMembers> invites);

  /**
   * <strong>채팅방 멤버 초대(참여) 쿼리 [단일]</strong>
   * <hr/>
   * <h3><채팅방 생성이 선행 되어야 합니다.></h3>
   * <h5>단일 유저 채팅방 참여시</h5>
   * <p>참여 하려는 채팅방 인덱스 번호 와 유저 인덱스 번호를 {@code member} 에 세팅하여 전달합니다.</p>
   * <hr/>
   * @param member {@code ChatMembers}
   */
  void addChatMember(ChatMembers member);
}
