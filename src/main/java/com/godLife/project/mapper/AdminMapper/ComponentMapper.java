package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ComponentMapper {

  // 목표 카테고리
  List<TargetCateDTO> targetCategoryList();                 // 목표 전체 카테고리 조회
  int insertTargetCategory(TargetCateDTO targetCateDTO);    // 목표 카테고리 등록
  int updateTargetCategory(TargetCateDTO targetCateDTO);    // 목표 카테고리 수정
  int deleteTargetCategory(int targetCateIdx);              // 목표 카테고리 삭제
  int countByTargetCateName(String targetCateName);         // 같은 이름의 카테고리 수를 반환 (중복 체크용)

  // 직업 카테고리 CRUD
  List<JobCateDTO> getAllJobCategories();        // 직업 카테고리 조회
  int insertJobCategory(JobCateDTO jobCateDTO);  // 직업 카테고리 추가
  int updateJobCategory(JobCateDTO jobCateDTO);  // 직업 카테고리 수정
  int deleteJobCategory(int jobIdx);             // 직업 카테고리 삭제
  int countByJobCateName(String jobCateName);    // 중복 체크용
}
