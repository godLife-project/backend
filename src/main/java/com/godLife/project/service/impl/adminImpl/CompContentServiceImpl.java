package com.godLife.project.service.impl.adminImpl;

import com.godLife.project.dto.categories.ChallengeCateDTO;
import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.mapper.AdminMapper.CompContentMapper;
import com.godLife.project.mapper.CategoryMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.AdminInterface.CompContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompContentServiceImpl implements CompContentService {
  private final CompContentMapper compContentMapper;
  private final CategoryMapper categoryMapper;
  private final RedisService redisService;


//                             목표 카테고리 관리 테이블


  //  Redis 캐시 갱신
  public void refreshTargetCache() {
    List<TargetCateDTO> target = categoryMapper.getAllTargetCategories(); // 또는 필요한 가공 포함
    redisService.saveListData("category::target", target, 'n', 0); // 영구 저장
  }

  //  목표 카테고리 추가
  @Override
  public int insertTargetCategory(TargetCateDTO targetCateDTO) {
    // 이미 같은 이름이 존재하는지 확인
    int duplicateCount = compContentMapper.countByTargetCateName(targetCateDTO.getName());

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    int result = compContentMapper.insertTargetCategory(targetCateDTO);
    refreshTargetCache();
    return result;
  }

  //  목표 카테고리 수정
  @Override
  public int updateTargetCategory(TargetCateDTO targetCateDTO) {
    int duplicateCount = compContentMapper.countTargetCateNameExceptSelf(
            targetCateDTO.getName(),
            (long) targetCateDTO.getIdx()
    );

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    int result =  compContentMapper.updateTargetCategory(targetCateDTO);
    refreshTargetCache();
    return result;
  }

  //  목표 카테고리 삭제
  public int softDeleteTargetCategory(int idx) {

    int result =  compContentMapper.softDeleteTargetCategory(idx);
    refreshTargetCache();
    return result;
  }


  //                             직업 카테고리 관리 테이블

  //  Redis 캐시 갱신
  public void refreshJobCache() {
    List<JobCateDTO> job = categoryMapper.getAllJOBCategories(); // 또는 필요한 가공 포함
    redisService.saveListData("category::job", job, 'n', 0); // 영구 저장
  }

  // 직업 카테고리 작성
  public int insertJobCategory(JobCateDTO jobCateDTO) {
    // 이미 같은 이름이 존재하는지 확인
    int duplicateCount = compContentMapper.countByJobCateName(jobCateDTO.getName());
    int result =  compContentMapper.insertJobCategory(jobCateDTO);
    refreshJobCache();
    return result;
  }

  // 직업 카테고리 수정
  public int updateJobCategory(JobCateDTO jobCateDTO) {

    int duplicateCount = compContentMapper.countJobCateNameExceptSelf(
            jobCateDTO.getName(),
            (long) jobCateDTO.getIdx()
    );

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    int result =  compContentMapper.updateJobCategory(jobCateDTO);
    refreshJobCache();
    return result;
  }

  // 직업 카테고리 삭제
  public int deleteJobCategory(int jobIdx) {

    int result =  compContentMapper.deleteJobCategory(jobIdx);
    refreshJobCache();
    return result;
  }


  //                            등급(불꽃) 관리 테이블
  //  Redis 캐시 갱신
  public void refreshFireCache() {
    List<FireDTO> fire = categoryMapper.getAllFireInfos(); // 또는 필요한 가공 포함
    redisService.saveListData("category::fire", fire, 'n', 0); // 영구 저장
  }

  // 등급(불꽃) 추가
  public int insertFire(FireDTO fireDTO) {
    // 등급 이름 중복 체크
    int duplicateCount = compContentMapper.countByFireName(fireDTO.getFireName());
    if (duplicateCount > 0) {
      throw new IllegalArgumentException("이미 존재하는 등급 이름입니다: " + fireDTO.getFireName());
    }

    // EXP 범위 중복 체크 (예: 겹치는 구간이 있으면 안 됨)
    int overlapCount = compContentMapper.countExpOverlap(fireDTO.getMinExp(), fireDTO.getMaxExp());
    if (overlapCount > 0) {
      throw new IllegalArgumentException("다른 등급과 EXP 범위가 겹칩니다.");
    }

    int result =  compContentMapper.insertFire(fireDTO);
    refreshFireCache();
    return result;
  }

  // 등급(불꽃) 수정
  public int updateFire(FireDTO fireDTO) {
    // 등급 이름 중복 체크 (자기 자신은 제외하고 체크)
    int duplicateCount = compContentMapper.countByTargetCateNameExceptId(
            fireDTO.getFireName(), fireDTO.getLvIdx()
    );
    if (duplicateCount > 0) {
      throw new IllegalArgumentException("이미 존재하는 등급 이름입니다: " + fireDTO.getFireName());
    }

    // EXP 범위 중복 체크 (자기 자신 제외)
    int overlapCount = compContentMapper.countExpOverlapExceptId(
            fireDTO.getMinExp(), fireDTO.getMaxExp(), fireDTO.getLvIdx()
    );
    if (overlapCount > 0) {
      throw new IllegalArgumentException("다른 등급과 EXP 범위가 겹칩니다.");
    }

    int result = compContentMapper.updateFire(fireDTO);
    refreshFireCache();
    return result;
  }

  // 등급(불꽃) 삭제
  public int deleteFire(int lvIdx) {

    int result = compContentMapper.deleteFire(lvIdx);
    refreshFireCache();
    return result;
  }


  //                            챌린지 카테고리 관리 테이블
  //  Redis 캐시 갱신
  public void refreshChallCache() {
    List<ChallengeCateDTO> chall = categoryMapper.getAllChallCategories(); // 또는 필요한 가공 포함
    redisService.saveListData("category::chall", chall, 'n', 0); // 영구 저장
  }


  //  챌린지 카테고리 추가
  @Override
  public int insertChallCate(ChallengeCateDTO challengeCateDTO) {
    // 이미 같은 이름이 존재하는지 확인
    int duplicateCount = compContentMapper.countByChallCateName(challengeCateDTO.getChallName());

    if (duplicateCount > 0) {
      return 409; // Conflict
    }
    int result = compContentMapper.insertChallCate(challengeCateDTO);
    refreshChallCache();
    return result;
  }

  //  챌린지 카테고리 수정
  @Override
  public int updateChallCate(ChallengeCateDTO challengeCateDTO) {
    int duplicateCount = compContentMapper.countByChallCateName(
            challengeCateDTO.getChallName()
    );

    if (duplicateCount > 0) {
      return 409; // Conflict
    }

    int result = compContentMapper.updateChallCate(challengeCateDTO);
    refreshChallCache();
    return result;
  }

  //  챌린지 카테고리 삭제
  public int deleteChallCate(int challCategoryIdx) {

    int result = compContentMapper.deleteChallCate(challCategoryIdx);
    refreshChallCache();
    return result;
  }


  /*
    // --------- 목표 카테고리 조회 ---------
  public List<TargetCateDTO> targetCategoryList() {
    return compContentMapper.targetCategoryList();
  }
  // --------- 직업 카테고리 조회 ---------
  public List<JobCateDTO> getAllJobCategories() {
    return compContentMapper.getAllJobCategories();
  }
  // ---------  등급(불꽃) 조회 ---------
  public List<FireDTO> selectAllFireGrades() {
    return compContentMapper.selectAllFireGrades();
  }
  // ---------  챌린지 조회  ---------
  public List<ChallengeCateDTO> selectChallCate(){
    return compContentMapper.selectChallCate();
  }
   */
}
