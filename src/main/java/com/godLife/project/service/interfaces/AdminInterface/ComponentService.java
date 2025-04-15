package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ComponentService {

  // 목표 카테고리
  List<TargetCateDTO> targetCategoryList();                   // 목표 전체 카테고리 조회
  int insertTargetCategory(TargetCateDTO targetCateDTO);     // 목표 카테고리 등록
  int updateTargetCategory(TargetCateDTO targetCateDTO);    // 목표 카테고리 수정
  int deleteTargetCategory(int targetCateIdx);             // 목표 카테고리 삭제

  // 직업 카테고리
  List<JobCateDTO> getAllJobCategories();          // 직업 카테고리 전체 조회
  int insertJobCategory(JobCateDTO jobCateDTO);    // 직업 카테고리 생성
  int updateJobCategory(JobCateDTO jobCateDTO);   // 직업 카테고리 수정
  int deleteJobCategory(int jobIdx);             // 직업 카테고리 삭제
}
