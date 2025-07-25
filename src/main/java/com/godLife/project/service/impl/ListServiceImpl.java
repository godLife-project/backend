package com.godLife.project.service.impl;

import com.godLife.project.dto.list.MyPlanDTO;
import com.godLife.project.dto.list.PlanListDTO;
import com.godLife.project.dto.list.QnaListDTO;
import com.godLife.project.dto.list.customDTOs.CustomPlanDTO;
import com.godLife.project.enums.QnaStatus;
import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.ListMapper;
import com.godLife.project.mapper.PlanMapper;
import com.godLife.project.service.interfaces.ListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListServiceImpl implements ListService {

  public final ListMapper listMapper;
  public final PlanMapper planMapper;

  @Override
  @Transactional
  public List<MyPlanDTO> getMyPlansList(int userIdx) {
    List<MyPlanDTO> myPlanList = new ArrayList<>();
    try {
      if (!planMapper.getUserIsDeleted(userIdx).contains("N")) { return null; } // 탈퇴한 유저 조회 불가

      List<CustomPlanDTO> planDtos = listMapper.getMyPlansByUserIdx(userIdx);

      if (planDtos == null || planDtos.isEmpty()) {
        return new ArrayList<>(); // 조회된 루틴 없을 때, 빈 리스트 보내기
      }

      for (CustomPlanDTO planDTO : planDtos) {
        MyPlanDTO myPlanDTO = new MyPlanDTO();
        int planIdx = planDTO.getPlanIdx();
        int targetIdx = listMapper.getTargetIdxByPlanIdx(planDTO.getPlanIdx());
        int jobIdx = listMapper.getJobIdxByPlanIdx(planIdx);

        myPlanDTO.setMyPlanInfos(planDTO); // 루틴 정보 추가
        myPlanDTO.setMyActivities(planMapper.detailActivityByPlanIdx(planIdx)); // 활동 정보 추가
        myPlanDTO.setTargetInfos(planMapper.getTargetCategoryByTargetIdx(targetIdx)); // 목표(관심사) 정보 추가
        myPlanDTO.setFireInfos(planMapper.detailFireByPlanIdx(planIdx)); // 불꽃 레벨 정보 추가

        // 직업 정보 추가
        if (jobIdx == 999) {
          myPlanDTO.setJobAddedInfos(planMapper.getJobEtcInfoByPlanIdx(planIdx));
        }
        else {
          myPlanDTO.setJobDefaultInfos(planMapper.getJOBCategoryByJobIdx(jobIdx));
        }

        // 리스트 추가
        myPlanList.add(myPlanDTO);
      }
      return myPlanList;
    } catch (Exception e) {
      log.error("e: ", e);
      return null; // 서버 에러 발생시 null 보내기
    }
  }

  @Override
  public Map<String, Object> getAllPlansList(String mode, int page, int size, int status,
                                             List<Integer> target, List<Integer> job,
                                             String sort, String order, String search, int userIdx) {
    // 페이지 번호가 음수일 경우 예외 처리
    if (page < 0) {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "유효하지 않은 페이지 번호");
      errorResponse.put("message", "페이지 번호는 1부터 시작 되어야 합니다.");
      return errorResponse;
    }
    try {
      int offset = page * size;

      //System.out.println("--서비스--");
      //System.out.println(page + " " +  size + " " + status + " " + target + " " + job + " " + sort + " " + order);

//      Map<String, List<String>> keywords = new HashMap<>();
//      if (search != null) {
//        keywords = parseKeywords(search);
//        System.out.println(keywords);
//      }

      List<PlanListDTO> plans = listMapper.getAllPlanList(mode, offset, size, status, target, job, sort, order, search, userIdx);
      int totalPlans = listMapper.getTotalPlanCount(mode, status, target, job, search, userIdx);

      Map<String, Object> response = new HashMap<>();
      response.put("plans", plans);
      response.put("currentPage", page + 1);
      response.put("totalPages", (int) Math.ceil((double) totalPlans / size));
      response.put("totalPosts", totalPlans);

      return response;
    } catch (Exception e) {
      log.error("e: ", e);
      // 예외 발생 시 반환할 응답
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "An error occurred while retrieving the plan list.");
      errorResponse.put("message", e.getMessage()); // 예외 메시지 추가
      errorResponse.put("status", "error"); // 에러 상태 추가
      return errorResponse; // 실패 응답 반환
    }
  }

  // 좋아요 한 루틴 리스트 조회
  @Override
  public Map<String, Object> getLikePlanList(String mode, int page, int size, int status, List<Integer> target,
                                      List<Integer> job, String order, String search, int userIdx) {
    // 페이지 번호가 음수일 경우 예외 처리
    if (page < 0) {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "유효하지 않은 페이지 번호");
      errorResponse.put("message", "페이지 번호는 1부터 시작 되어야 합니다.");
      return errorResponse;
    }
    try {
      int offset = page * size;

      //System.out.println("--서비스--");
      //System.out.println(page + " " +  size + " " + status + " " + target + " " + job + " " + sort + " " + order);

//      Map<String, List<String>> keywords = new HashMap<>();
//      if (search != null) {
//        keywords = parseKeywords(search);
//        System.out.println(keywords);
//      }
      List<PlanListDTO> plans = listMapper.getLikePlanList(mode, offset, size, target, job, order, search, userIdx);
      int totalPlans = listMapper.getTotalPlanCount(mode, status, target, job, search, userIdx);

      Map<String, Object> response = new HashMap<>();
      response.put("plans", plans);
      response.put("currentPage", page + 1);
      response.put("totalPages", (int) Math.ceil((double) totalPlans / size));
      response.put("totalPosts", totalPlans);

      return response;
    } catch (Exception e) {
      log.error("e: ", e);
      // 예외 발생 시 반환할 응답
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "An error occurred while retrieving the plan list.");
      errorResponse.put("message", e.getMessage()); // 예외 메시지 추가
      errorResponse.put("status", "error"); // 에러 상태 추가
      return errorResponse; // 실패 응답 반환
    }
  }

  @Override
  public Map<String, Object> getQnaList(int qUserIdx, int page, int size, String status, String sort, String order, String search) {
    try {
      // 페이지 번호가 음수일 경우 예외 처리
      if (page < 0) {
        throw new CustomException("페이지 번호는 1부터 시작 되어야 합니다.", HttpStatus.BAD_REQUEST);
      }
      if (size < 9) {
        throw new CustomException("조회 할 최소 문의 수는 10 이상 이어야 합니다.", HttpStatus.BAD_REQUEST);
      }

      int offset = page * size;
      String notStatus = QnaStatus.DELETED.getStatus();

//      Map<String, List<String>> keywords = new HashMap<>();
//      if (search != null) {
//        keywords = parseKeywords(search);
//      }

      List<QnaListDTO> QnAs = listMapper.getQnaList(qUserIdx, notStatus, offset, size, status, sort, order, search);

      if (QnAs == null || QnAs.isEmpty()) {
        throw new CustomException("문의가 존재하지 않습니다.", HttpStatus.NO_CONTENT);
      }

      int totalQna = listMapper.getTotalQnaCount(qUserIdx, notStatus, status, search);

      Map<String, Object> response = new HashMap<>();
      response.put("QnAs", QnAs);
      response.put("currentPage", page + 1);
      response.put("totalPages", (int) Math.ceil((double) totalQna / size));
      response.put("totalPosts", totalQna);

      return response;

    } catch (CustomException e) {
      log.info("ListService - getQnaList :: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("ListService - getQnaList ::", e);
      throw e;
    }
  }



  /* -----------------------------------------// 함수 구현 //------------------------------------------------------- */
  public static Map<String, List<String>> parseKeywords(String input) {
    Map<String, List<String>> keywords = new HashMap<>();
    if (input == null || input.isBlank()) return keywords;

    String remainingInput = input.trim();

    // 1. "!^" 처리 (NOT 조건 - 여러 개 처리)
    List<String> notConditions = new ArrayList<>();
    String[] notSplit = remainingInput.split("!\\^");
    remainingInput = notSplit[0].trim(); // 첫 번째는 검색 본문
    for (int i = 1; i < notSplit.length; i++) {
      String notKeyword = notSplit[i].trim();
      if (!notKeyword.isBlank()) {
        notConditions.add(notKeyword);
      }
    }
    if (!notConditions.isEmpty()) {
      keywords.put("not", notConditions);
    }

    // 2. "|" 처리 (OR 조건)
    List<String> orConditions = new ArrayList<>();
    List<String> andConditions = new ArrayList<>();

    String[] orSplit = remainingInput.split("\\|");
    for (String orPart : orSplit) {
      orPart = orPart.trim();
      if (orPart.contains("+")) {
        // 3. "+" 처리 (AND 조건)
        andConditions.add(orPart.replaceAll("\\+", "%"));
      } else {
        orConditions.add(orPart);
      }
    }

    // 결과 저장
    if (!orConditions.isEmpty()) {
      keywords.put("or", orConditions);
    }
    if (!andConditions.isEmpty()) {
      keywords.put("and", andConditions);
    }

    return keywords;
  }
  /* --------------------------------------------------------------------------------------------------------------- */

}
