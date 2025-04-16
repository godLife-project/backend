package com.godLife.project.service.impl.AdminImpl;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.mapper.AdminMapper.ComponentMapper;
import com.godLife.project.service.interfaces.AdminInterface.ComponentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentServiceImpl implements ComponentService {
  private ComponentMapper componentMapper;

  public ComponentServiceImpl(ComponentMapper componentMapper){this.componentMapper = componentMapper;}

  //                             목표 카테고리 관리 테이블

  // --------- 목표 카테고리 조회 ---------
  public List<TargetCateDTO> targetCategoryList(){
    return componentMapper.targetCategoryList();
  }
  //  목표 카테고리 추가
  @Override
  public int insertTargetCategory(TargetCateDTO targetCateDTO) {
    // 이미 같은 이름이 존재하는지 확인
    int duplicateCount = componentMapper.countByTargetCateName(targetCateDTO.getName());

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    return componentMapper.insertTargetCategory(targetCateDTO);
  }
  //  목표 카테고리 수정
  @Override
  public int updateTargetCategory(TargetCateDTO targetCateDTO) {
    int duplicateCount = componentMapper.countTargetCateNameExceptSelf(
            targetCateDTO.getName(),
            (long) targetCateDTO.getIdx()
    );

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    return componentMapper.updateTargetCategory(targetCateDTO);
  }

  //  목표 카테고리 삭제
  public int deleteTargetCategory(int targetCateIdx){
    return componentMapper.deleteTargetCategory(targetCateIdx);
  }


  //                             직업 카테고리 관리 테이블

  // 직업 카테고리 조회
  public List<JobCateDTO> getAllJobCategories() {
    return componentMapper.getAllJobCategories();
  }

  // 직업 카테고리 작성
  public int insertJobCategory(JobCateDTO jobCateDTO) {
    // 이미 같은 이름이 존재하는지 확인
    int duplicateCount = componentMapper.countByTargetCateName(jobCateDTO.getName());
    return componentMapper.insertJobCategory(jobCateDTO);
  }

  // 직업 카테고리 수정
  public int updateJobCategory(JobCateDTO jobCateDTO) {
    int duplicateCount = componentMapper.countJobCateNameExceptSelf(
            jobCateDTO.getName(),
            (long) jobCateDTO.getIdx()
    );

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    return componentMapper.updateJobCategory(jobCateDTO);
  }

  // 직업 카테고리 삭제
  public int deleteJobCategory(int jobIdx) {
    return componentMapper.deleteJobCategory(jobIdx);
  }


  //                            등급(불꽃) 관리 테이블

  // 등급(불꽃) 조회
  public List<FireDTO> selectAllFireGrades(){
    return componentMapper.selectAllFireGrades();
  }

  // 등급(불꽃) 추가
  public int insertFire(FireDTO fireDTO) {
    // 등급 이름 중복 체크
    int duplicateCount = componentMapper.countByFireName(fireDTO.getFireName());
    if (duplicateCount > 0) {
      throw new IllegalArgumentException("이미 존재하는 등급 이름입니다: " + fireDTO.getFireName());
    }

    // EXP 범위 중복 체크 (예: 겹치는 구간이 있으면 안 됨)
    int overlapCount = componentMapper.countExpOverlap(fireDTO.getMinExp(), fireDTO.getMaxExp());
    if (overlapCount > 0) {
      throw new IllegalArgumentException("다른 등급과 EXP 범위가 겹칩니다.");
    }

    return componentMapper.insertFire(fireDTO);
  }

  // 등급(불꽃) 수정
  public int updateFire(FireDTO fireDTO) {
    // 등급 이름 중복 체크 (자기 자신은 제외하고 체크)
    int duplicateCount = componentMapper.countByTargetCateNameExceptId(
            fireDTO.getFireName(), fireDTO.getLvIdx()
    );
    if (duplicateCount > 0) {
      throw new IllegalArgumentException("이미 존재하는 등급 이름입니다: " + fireDTO.getFireName());
    }

    // EXP 범위 중복 체크 (자기 자신 제외)
    int overlapCount = componentMapper.countExpOverlapExceptId(
            fireDTO.getMinExp(), fireDTO.getMaxExp(), fireDTO.getLvIdx()
    );
    if (overlapCount > 0) {
      throw new IllegalArgumentException("다른 등급과 EXP 범위가 겹칩니다.");
    }

    return componentMapper.updateFire(fireDTO);
  }

  // 등급(불꽃) 삭제
  public int deleteFire(int lvIdx) {
    return componentMapper.deleteFire(lvIdx);
  }
}
