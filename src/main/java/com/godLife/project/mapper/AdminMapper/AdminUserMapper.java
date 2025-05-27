package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.categories.AuthorityCateDTO;
import com.godLife.project.dto.list.customDTOs.AdminListDTO;
import com.godLife.project.dto.list.customDTOs.AdminUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    List<AdminUserDTO> selectAllActiveUsers(@Param("startRow")int startRow, @Param("endRow")int endRow);      // 유저 정보 리스트 조회
    int countAllActiveUsers();                     // 전체 유저 수 조회 (페이징)
    int banUserByIdx(int userIdx);                // 유저 정지

    // 권한관리
    List<AuthorityCateDTO> getAuthorityList();                      // 권한 목록 조회
    List<AdminUserDTO> getUserByAuthority(int authorityIdx);        // 권한별 유저 목록 조회
    List<AdminListDTO> adminList();                                 // 관리자 명단 조회
    int updateUserAuthority(@Param("userIdx") int userIdx, @Param("authorityIdx")int authorityIdx);  // 유저 권한 변경
}
