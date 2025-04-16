package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.datas.FireDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ComponentMapper {

  // 목표 카테고리
  List<TargetCateDTO> targetCategoryList();                 // 목표 전체 카테고리 조회
  int insertTargetCategory(TargetCateDTO targetCateDTO);    // 목표 카테고리 등록
  int updateTargetCategory(TargetCateDTO targetCateDTO);    // 목표 카테고리 수정
  int deleteTargetCategory(int targetCateIdx);              // 목표 카테고리 삭제
  int countByTargetCateName(String targetCateName);         // 같은 이름의 카테고리 수를 반환 (중복 체크용)
  int countTargetCateNameExceptSelf(@Param("name") String name, @Param("idx") Long idx); //수정시 중복체크할 때 자기자신 제외


  // 직업 카테고리 CRUD
  List<JobCateDTO> getAllJobCategories();        // 직업 카테고리 조회
  int insertJobCategory(JobCateDTO jobCateDTO);  // 직업 카테고리 추가
  int updateJobCategory(JobCateDTO jobCateDTO);  // 직업 카테고리 수정
  int deleteJobCategory(int jobIdx);             // 직업 카테고리 삭제
  int countByJobCateName(String jobCateName);    // 중복 체크용
  int countJobCateNameExceptSelf(@Param("name") String name, @Param("idx") Long idx); // 수정시 중복체크할 때 자기자신 제외

  // 등급(불꽃) 테이블 관리 CRUD
  List<FireDTO> selectAllFireGrades();          // 등급(불꽃) 조회
  int insertFire(FireDTO fireDTO);              // 등급(불꽃) 추가
  int updateFire(FireDTO fireDTO);              // 등급(불꽃) 수정
  int deleteFire(int lvIdx);                     // 등급(불꽃) 삭제
  //                        중복체크용
  // 이름 중복 체크 (추가용)
  int countByFireName(String fireName);
  // 이름 중복 체크 (수정용 - 현재 ID 제외)
  int countByTargetCateNameExceptId(String fireName, Long lvIdx);
  // EXP 범위 중복 체크 (추가용)
  int countExpOverlap(int minExp, int maxExp);
  // EXP 범위 중복 체크 (수정용 - 현재 ID 제외)
  int countExpOverlapExceptId(int minExp, int maxExp, Long lvIdx);

}
