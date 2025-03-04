package com.godLife.project.service.impl;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.mapper.CategoryMapper;
import com.godLife.project.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    // 탑메뉴 카테고리 조회
    @Override
    public List<TopCateDTO> getAllTopCategories() {
        return categoryMapper.getAllTopCategories();
    }
    // 직업 카테고리 조회
    @Override
    public List<JobCateDTO> getAllJobCategories() {return categoryMapper.getAllJOBCategories(); }
    // 관심사 카테고리 조회
    @Override
    public  List<TargetCateDTO> getAllTargetCategories() { return categoryMapper.getAllTargetCategories(); }
    // 챌린지 카테고리 조회
    @Override
    public List<ChallengeCateDTO> getAllChallCategories() { return categoryMapper.getAllChallCategories(); }
    // 숏컷 카테고리
    @Override
    public List<ShortCutCateDTO> getAllShortCategories() { return categoryMapper.getAllShortCategories(); }
    // 권한 카테고리
    @Override
    public List<AuthorityCateDTO> getAllAuthorityCategories() { return categoryMapper.getAllAuthorityCategories(); }
    // 아이콘 정보 조회
    @Override
    public List<IconDTO> getUserIconInfos() { return categoryMapper.getUserIconInfos(); }
    // 아이콘 정보 조회 ( 관리자 용 )
    @Override
    public List<IconDTO> getAllIconInfos() { return categoryMapper.getAllIconInfos(); }
}
