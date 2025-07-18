package com.godLife.project.service.impl.adminImpl;

import com.godLife.project.dto.chat.ChatMembers;
import com.godLife.project.dto.chat.ChatRoles;
import com.godLife.project.dto.chat.ChatRoomDTO;
import com.godLife.project.dto.request.adminChat.ChatCreateDTO;
import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.AdminMapper.AdminChatMapper;
import com.godLife.project.mapper.UserMapper;
import com.godLife.project.service.interfaces.AdminInterface.AdminChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminChatServiceImpl implements AdminChatService {

  private final AdminChatMapper adminChatMapper;
  private final UserMapper userMapper;

  /// 채팅방 생성 로직
  @Transactional(rollbackFor = Exception.class)
  public int createAdminChatRoom(ChatCreateDTO chatCreateDTO) {
    try {
      // 채팅방 제목
      String name = chatCreateDTO.getName();
      // 공개 채팅방 여부
      boolean isPublic = chatCreateDTO.isPublicRoom();
      // 채팅방 개설자
      int createdBy = chatCreateDTO.getCreatedBy();
      // 권한 및 초대 멤버
      List<Integer> roleIndexes = chatCreateDTO.getRoleIndexes();
      List<String> invites = chatCreateDTO.getInvites();

      boolean isNotRoleExist = roleIndexes == null || roleIndexes.isEmpty();
      boolean isNotInviteExist = invites == null || invites.isEmpty();
      boolean nameIsBlank = name == null || name.isBlank();

      // 초대 멤버 이메일 - 인덱스 매핑
      Map<String, Integer> userMap = new HashMap<>();

      // 유효성 검사 및 이름 자동 설정
      if (!isNotInviteExist) {
        Set<String> seen = new HashSet<>();
        for (String invite : invites) {
          if (seen.contains(invite)) {
            throw new CustomException("동일한 관리자를 중복 초대할 수 없습니다.", HttpStatus.CONFLICT);
          } else {
            seen.add(invite);
          }
        }
      }

      if (isPublic) {
        if (nameIsBlank) {
          throw new CustomException("공개 채팅방은 채팅방 이름 설정이 필수 입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!isNotRoleExist && !isNotInviteExist) {
          for (String invite : invites) {
            int adminIdx = userMapper.getUserIdxByUserEmail(invite);
            userMap.put(invite, adminIdx); // 이메일과 인덱스 매칭하여 미리 저장

            int roleIdx = userMapper.getUserAuthority(adminIdx);

            if (!roleIndexes.contains(roleIdx)) {
              throw new CustomException("채팅방 접근 권한과 부합하지 않은 관리자는 초대할 수 없습니다.", HttpStatus.BAD_REQUEST);
            }
          }
        }
      } else {
        // 비공개 채팅방: 초대 인원이 1명 이상 필요
        if (isNotInviteExist) {
          throw new CustomException("비공개 채팅방은 멤버 초대가 최소 1명 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }

        // 이름이 없으면 첫 번째 유저 이름 기준으로 자동 생성
        if (nameIsBlank) {
          name = userMapper.getUserNameByUserEmail(invites.get(0));
          if (invites.size() > 1) {
            name += " 외 " + invites.size() + "명";
          }
          chatCreateDTO.setName(name); // DTO에 이름 반영
        }
      }

      // 채팅방 생성용 DTO 구성
      ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
      chatRoomDTO.setName(name);
      chatRoomDTO.setIsPublic(isPublic ? 1 : 0);
      chatRoomDTO.setCreatedBy(createdBy);

      /// 채팅방 생성
      adminChatMapper.createChatRoom(chatRoomDTO);

      // 생성한 채팅방의 인덱스 번호 저장
      int roomIdx = chatRoomDTO.getRoomIdx();

      /// 공개방 접근 권한 설정
      boolean isPublicNRoleExist = isPublic && !isNotRoleExist;
      if (isPublicNRoleExist) {
        for (int roleIdx : roleIndexes) {
          ChatRoles chatRolesDTO = new ChatRoles();
          chatRolesDTO.setRoomIdx(roomIdx);
          chatRolesDTO.setRoleIdx(roleIdx);

          adminChatMapper.setRolesToChatRoom(chatRolesDTO);
        }
      }

      /// 초대 유저 설정
      if (!isNotInviteExist) {
        for (String invite : invites) {
          int adminIdx = userMap.isEmpty() ? userMapper.getUserIdxByUserEmail(invite) : userMap.get(invite);

          userInvite(roomIdx, adminIdx);
        }
      }

      userInvite(roomIdx, createdBy); // 본인도 참가

      return roomIdx;

    } catch (CustomException e) {
      log.info(e.getMessage());
      throw e;

    } catch (Exception e) {
      log.error("AdminChatService - createAdminChatRoom :: 채팅방 생성 중 예기치 못한 에러가 발생했습니다.", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw e;
    }
  }

  /**
   * 유저 초대 함수
   * @param roomIdx 채팅방 고유 인덱스 번호
   * @param adminIdx 초대할 유저 고유 인덱스 번호
   */
  private void userInvite(int roomIdx, int adminIdx) {
    ChatMembers chatMembersDTO = new ChatMembers();
    chatMembersDTO.setRoomIdx(roomIdx);
    chatMembersDTO.setAdminIdx(adminIdx);

    adminChatMapper.addChatMember(chatMembersDTO);
  }
}
