package com.godLife.project.service.impl;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.dto.datas.UserLevelDTO;
import com.godLife.project.mapper.CategoryMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final RedisService redisService;


    // 탑메뉴 카테고리 조회
    @Override
    public List<TopCateDTO> getAllTopCategories() {
        List<TopCateDTO> data = redisService.getListData("category::topMenu", TopCateDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllTopCategories();
        }
        return data;
    }
    // 직업 카테고리 조회
    @Override
    public List<JobCateDTO> getAllJobCategories() {
        List<JobCateDTO> data = redisService.getListData("category::job", JobCateDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllJOBCategories();
        }
        return data;
    }
    // 관심사 카테고리 조회
    @Override
    public  List<TargetCateDTO> getAllTargetCategories() {
        List<TargetCateDTO> data = redisService.getListData("category::target", TargetCateDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllTargetCategories();
        }
        return data;
    }
    // 챌린지 카테고리 조회
    @Override
    public List<ChallengeCateDTO> getAllChallCategories() {
        List<ChallengeCateDTO> data = redisService.getListData("category::chall", ChallengeCateDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllChallCategories();
        }
        return data;
    }
    // 숏컷 카테고리
    @Override
    public List<ShortCutCateDTO> getAllShortCategories() { return categoryMapper.getAllShortCategories(); }
    // 권한 카테고리
    @Override
    public List<AuthorityCateDTO> getAllAuthorityCategories() { return categoryMapper.getAllAuthorityCategories(); }
    // 아이콘 정보 조회
    @Override
    public List<IconDTO> getUserIconInfos() {
        List<IconDTO> data = redisService.getListData("category::userIcon", IconDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getUserIconInfos();
        }
        return data;
    }
    // 아이콘 정보 조회 ( 관리자 용 )
    @Override
    public List<IconDTO> getAllIconInfos() {
        List<IconDTO> data = redisService.getListData("category::adminIcon", IconDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllIconInfos();
        }
        return data;
    }

    // 불꽃 정보 조회
    @Override
    public List<FireDTO> getAllFireInfos() {
        List<FireDTO> data = redisService.getListData("category::fire", FireDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllFireInfos();
        }
        return data;
    }
    // 유저 레벨 정보 조회
    @Override
    public List<UserLevelDTO> getAllUserLevelInfos() {
        List<UserLevelDTO> data = redisService.getListData("category::userLv", UserLevelDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllUserLevelInfos();
        }
        return data;
    }
}
