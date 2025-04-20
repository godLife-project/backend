package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.categories.FaqCateDTO;
import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.QnaCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.datas.FireDTO;
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

  // 등급(불꽃) 관리 테이블
  List<FireDTO> selectAllFireGrades();            // 등급(불꽃) 조회
  int insertFire(FireDTO fireDTO);                // 등급(불꽃) 추가
  int updateFire(FireDTO fireDTO);                // 등급(불꽃) 수정
  int deleteFire(int lvIdx);                      // 등급(불꽃) 삭제

  // FAQ 카테고리 관리
  List<FaqCateDTO> selectFaqCate();               // FAQ 카테고리 LIST 조회
  int insertFaqCate(FaqCateDTO faqCateDTO);       // FAQ 카테고리 추가
  int updateFaqCate(FaqCateDTO faqCateDTO);       // FAQ 카테고리 수정
  int deleteFaqCate(int faqCategoryIdx);          // FAQ 카테고리 삭제

  // QNA 카테고리 관리
  List<QnaCateDTO> selectQnaCate();               // FAQ 카테고리 LIST 조회
  int insertQnaCate(QnaCateDTO qnaCateDTO);       // FAQ 카테고리 추가
  int updateQnaCate(QnaCateDTO qnaCateDTO);       // FAQ 카테고리 수정
  int deleteQnaCate(int qnaCategoryIdx);          // FAQ 카테고리 삭제

}
