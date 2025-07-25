package com.godLife.project.service.impl.adminImpl;

import com.godLife.project.dto.categories.AuthorityCateDTO;
import com.godLife.project.dto.list.customDTOs.AdminListDTO;
import com.godLife.project.dto.list.customDTOs.AdminUserDTO;
import com.godLife.project.mapper.AdminMapper.AdminUserMapper;
import com.godLife.project.service.interfaces.AdminInterface.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;

    // 유저 리스트 조회
    public List<AdminUserDTO> getPagedUserList(int page, int size) {
        int offset = (page - 1) * size;
      return adminUserMapper.selectAllActiveUsers(offset, size);
    }

    // 페이징
    public int countAllActiveUsers() {
        return adminUserMapper.countAllActiveUsers();
    }


    // 유저 정지
    public int banUser(int userIdx) {
        return adminUserMapper.banUserByIdx(userIdx);
    }

    //                       권한 관리

    // 권한 조회
    @Override
    public List<AuthorityCateDTO> getAuthorityList() {
        return adminUserMapper.getAuthorityList();
    }

    // 권한별 유저 조회
    @Override
    public List<AdminUserDTO> getUsersByAuthority(int authorityIdx) {
        return adminUserMapper.getUserByAuthority(authorityIdx);
    }

    // 관리자 명단 조회
    public List<AdminListDTO> adminList(){return adminUserMapper.adminList();}

    // 권한 변경
    @Override
    public int updateUserAuthority(int userIdx, int authorityIdx) {
        return adminUserMapper.updateUserAuthority(userIdx, authorityIdx);
    }
}
