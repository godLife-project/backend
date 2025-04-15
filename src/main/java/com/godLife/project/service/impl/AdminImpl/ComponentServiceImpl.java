package com.godLife.project.service.impl.AdminImpl;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.mapper.AdminMapper.ComponentMapper;
import com.godLife.project.service.interfaces.AdminInterface.ComponentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentServiceImpl implements ComponentService {
  private ComponentMapper componentMapper;

  public ComponentServiceImpl(ComponentMapper componentMapper){this.componentMapper = componentMapper;}

  //                             목표 카테고리
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
    int duplicateCount = componentMapper.countByTargetCateName(targetCateDTO.getName());

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    return componentMapper.updateTargetCategory(targetCateDTO);
  }

  //  목표 카테고리 삭제
  public int deleteTargetCategory(int targetCateIdx){
    return componentMapper.deleteTargetCategory(targetCateIdx);
  }


  //                             직업 카테고리
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
    // 이미 같은 이름이 존재하는지 확인
    int duplicateCount = componentMapper.countByTargetCateName(jobCateDTO.getName());
    return componentMapper.updateJobCategory(jobCateDTO);
  }

  // 직업 카테고리 삭제
  public int deleteJobCategory(int jobIdx) {
    return componentMapper.deleteJobCategory(jobIdx);
  }
}
