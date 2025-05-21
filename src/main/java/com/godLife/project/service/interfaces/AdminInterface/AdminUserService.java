package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.categories.AuthorityCateDTO;
import com.godLife.project.dto.list.customDTOs.AdminListDTO;
import com.godLife.project.dto.list.customDTOs.AdminUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminUserService {
    List<AdminUserDTO> getPagedUserList(int page, int size);
    int countAllActiveUsers();
    int banUser(int userIdx);     // 유저 정지

    // 권한 관리
    List<AuthorityCateDTO> getAuthorityList();
    List<AdminUserDTO> getUsersByAuthority(int authorityIdx);
    List<AdminListDTO> adminList();
    int updateUserAuthority(int userIdx, int authorityIdx);

}
