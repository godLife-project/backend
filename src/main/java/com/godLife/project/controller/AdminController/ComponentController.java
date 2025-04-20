package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.categories.FaqCateDTO;
import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.QnaCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.ComponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/component")
public class ComponentController {

  @Autowired
  private GlobalExceptionHandler handler;

  @Autowired
  private final ComponentService componentService;

  public ComponentController(ComponentService componentService) {
    this.componentService = componentService;
  }

  //                                  목표 카테고리

  // 목표 카테고리 조회
  @GetMapping("targetCategory")
  public ResponseEntity<Map<String, Object>> targetCategoryList() {
    try {
      List<TargetCateDTO> categoryList = componentService.targetCategoryList();

      if (categoryList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "등록된 목표 카테고리가 없습니다."));
      }

      Map<String, Object> response = handler.createResponse(200, "목표 카테고리 조회 성공");
      response.put("categories", categoryList);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("목표 카테고리 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 카테고리 조회에 실패했습니다."));
    }
  }

  // 목표 카테고리 추가
  @PostMapping("targetCategory")
  public ResponseEntity<Map<String, Object>> createCategory(@RequestBody TargetCateDTO targetCateDTO) {
    try {
      int result = componentService.insertTargetCategory(targetCateDTO);

      if (result == 409) {
        return ResponseEntity.status(handler.getHttpStatus(409))
                .body(handler.createResponse(409, "이미 존재하는 카테고리 이름입니다."));
      }

      return ResponseEntity.status(handler.getHttpStatus(201))
              .body(handler.createResponse(201, "목표 카테고리 등록 성공"));

    } catch (Exception e) {
      log.error("목표 카테고리 등록 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "목표 카테고리 등록 중 서버 오류가 발생했습니다."));
    }
  }

  // 목표 카테고리 수정
  @PatchMapping("/targetCategory/{targetCateIdx}")
  public ResponseEntity<Map<String, Object>> updateCategory(
          @PathVariable int targetCateIdx,
          @RequestBody TargetCateDTO targetCateDTO) {
    targetCateDTO.setIdx(targetCateIdx);
    try {
      int result = componentService.updateTargetCategory(targetCateDTO);

      if (result == 409) {
        return ResponseEntity.status(handler.getHttpStatus(409))
                .body(handler.createResponse(409, "이미 존재하는 카테고리 이름입니다."));
      }
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "목표 카테고리 수정 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "수정할 카테고리를 찾을 수 없습니다."));
      }

    } catch (Exception e) {
      log.error("목표 카테고리 수정 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "목표 카테고리 수정 중 서버 오류가 발생했습니다."));
    }
  }

  // 목표 카테고리 삭제
  @DeleteMapping("targetCategory/{targetCateIdx}")
  public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable int targetCateIdx) {
    try {
      int result = componentService.deleteTargetCategory(targetCateIdx);

      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "목표 카테고리 삭제 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "삭제할 카테고리를 찾을 수 없습니다."));
      }

    } catch (Exception e) {
      log.error("목표 카테고리 삭제 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "목표 카테고리 삭제 중 서버 오류가 발생했습니다."));
    }
  }


  //                                  직업 카테고리
  // 직업 카테고리 조회
  @GetMapping("jobCategory")
  public ResponseEntity<Map<String, Object>> jobCategoryList() {
    try {
      List<JobCateDTO> jobCategoryList = componentService.getAllJobCategories();

      if (jobCategoryList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "등록된 직업 카테고리가 없습니다."));
      }

      Map<String, Object> response = handler.createResponse(200, "직업 카테고리 조회 성공");
      response.put("categories", jobCategoryList);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("직업 카테고리 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 카테고리 조회에 실패했습니다."));
    }
  }

  // 직업 카테고리 추가
  @PostMapping("/jobCategory")
  public ResponseEntity<Map<String, Object>> createJobCategory(@RequestBody JobCateDTO jobCateDTO) {
    try {
      int result = componentService.insertJobCategory(jobCateDTO);
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "직업 카테고리 생성 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(400))
                .body(handler.createResponse(400, "직업 카테고리 생성 실패"));
      }
    } catch (Exception e) {
      log.error("직업 카테고리 생성 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // 직업 카테고리 수정
  @PatchMapping("/jobCategory/{jobIdx}")
  public ResponseEntity<Map<String, Object>> updateJobCategory(
          @PathVariable("jobIdx") int jobIdx,
          @RequestBody JobCateDTO jobCateDTO) {
    try {
      jobCateDTO.setIdx(jobIdx);
      int result = componentService.updateJobCategory(jobCateDTO);

      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "직업 카테고리 수정 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "수정할 카테고리를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("직업 카테고리 수정 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // 직업 카테고리 삭제
  @DeleteMapping("/jobCategory/{jobIdx}")
  public ResponseEntity<Map<String, Object>> deleteJobCategory(@PathVariable("jobIdx") int jobIdx) {
    try {
      int result = componentService.deleteJobCategory(jobIdx);
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "직업 카테고리 삭제 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "삭제할 카테고리를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("직업 카테고리 삭제 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }


  //                                  등급(불꽃) 관리 테이블
  @GetMapping("fire")
  public ResponseEntity<Map<String, Object>> selectAllFireGrades(FireDTO fireDTO) {
    try {
      List<FireDTO> fireDTOList = componentService.selectAllFireGrades();

      if (fireDTOList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "등록된 등급(불꽃)이 없습니다."));
      }

      Map<String, Object> response = handler.createResponse(200, "등급(불꽃) 조회 성공");
      response.put("fire", fireDTOList);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("등급(불꽃) 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 등급(불꽃) 조회에 실패했습니다."));
    }
  }

  // 등급(불꽃) 추가
  @PostMapping("fire")
  public ResponseEntity<Map<String, Object>> insertFire(@RequestBody FireDTO fireDTO) {
    try {
      int result = componentService.insertFire(fireDTO);

      if (result == 409) {
        return ResponseEntity.status(handler.getHttpStatus(409))
                .body(handler.createResponse(409, "이미 존재하는 등급 이름입니다."));
      }

      return ResponseEntity.status(handler.getHttpStatus(201))
              .body(handler.createResponse(201, "등급(불꽃) 등록 성공"));

    } catch (Exception e) {
      log.error("등급(불꽃) 등록 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "등급(불꽃) 등록 중 서버 오류가 발생했습니다."));
    }
  }

  // 등급(불꽃) 수정
  @PatchMapping("/fire/{lvIdx}")
  public ResponseEntity<Map<String, Object>> updateFire(
          @PathVariable("lvIdx") int lvIdx,
          @RequestBody FireDTO fireDTO) {
    try {
      fireDTO.setLvIdx((long) lvIdx);
      int result = componentService.updateFire(fireDTO);

      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "등급(불꽃) 수정 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "수정할 등급(불꽃)을 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("불꽃(등급) 수정 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // 등급(불꽃) 삭제
  @DeleteMapping("/fire/{lvIdx}")
  public ResponseEntity<Map<String, Object>> deleteFire(@PathVariable("lvIdx") int lvIdx) {
    try {
      int result = componentService.deleteFire(lvIdx);
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "등급(불꽃) 삭제 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "삭제할 등급(불꽃)을 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("등급(불꽃) 삭제 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }


  //                                  FAQ 카테고리 관리 테이블
  // FAQ 카테고리 조회
  @GetMapping("/faqCategory")
  public ResponseEntity<Map<String, Object>> selectFaqCate() {
    try {
      List<FaqCateDTO> faqCateDTOList = componentService.selectFaqCate();

      if (faqCateDTOList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "등록된 FAQ 카테고리가 없습니다."));
      }

      Map<String, Object> response = handler.createResponse(200, "FAQ 카테고리 조회 성공");
      response.put("faqCategory", faqCateDTOList);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("FAQ 카테고리 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 FAQ 카테고리 조회에 실패했습니다."));
    }
  }

  // FAQ 추가
  @PostMapping("/faqCategory")
  public ResponseEntity<Map<String, Object>> insertFaqCate(@RequestBody FaqCateDTO faqCateDTO) {
    try {
      int result = componentService.insertFaqCate(faqCateDTO);

      if (result == 409) {
        return ResponseEntity.status(handler.getHttpStatus(409))
                .body(handler.createResponse(409, "이미 존재하는 FAQ 카테고리 이름입니다."));
      }

      return ResponseEntity.status(handler.getHttpStatus(201))
              .body(handler.createResponse(201, "FAQ 카테고리 등록 성공"));

    } catch (Exception e) {
      log.error("FAQ 카테고리 등록 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "FAQ 카테고리 등록 중 서버 오류가 발생했습니다."));
    }
  }

  // FAQ 카테고리 수정
  @PatchMapping("/faqCategory/{faqCategoryIdx}")
  public ResponseEntity<Map<String, Object>> updateFaqCate(@PathVariable("faqCategoryIdx")int faqCategoryIdx,
                                                           @RequestBody FaqCateDTO faqCateDTO){
    try {
      faqCateDTO.setFaqCategoryIdx(faqCategoryIdx);
      int result = componentService.updateFaqCate(faqCateDTO);

      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "FAQ 카테고리 수정 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "수정할 FAQ 카테고리를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("FAQ 카테고리 수정 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // FAQ 카테고리 삭제
  @DeleteMapping("faqCategory/{faqCategoryIdx}")
  public ResponseEntity<Map<String, Object>> deleteFaqCate(@PathVariable("faqCategoryIdx") int faqCategoryIdx){
    try {
      int result = componentService.deleteFaqCate(faqCategoryIdx);
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "FAQ 카테고리 삭제 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "삭제할 FAQ 카테고리를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("FAQ 카테고리 삭제 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }


  //                                  QNA 카테고리 관리 테이블
  // QNA 카테고리 조회
  @GetMapping("/qnaCategory")
  public ResponseEntity<Map<String, Object>> selectQnaCate() {
    try {
      List<QnaCateDTO> qnaCateDTOList = componentService.selectQnaCate();

      if (qnaCateDTOList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "등록된 QNA 카테고리가 없습니다."));
      }

      Map<String, Object> response = handler.createResponse(200, "QNA 카테고리 조회 성공");
      response.put("faqCategory", qnaCateDTOList);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("FAQ 카테고리 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 QNA 카테고리 조회에 실패했습니다."));
    }
  }

  // QNA 추가
  @PostMapping("/qnaCategory")
  public ResponseEntity<Map<String, Object>> insertQnaCate(@RequestBody QnaCateDTO qnaCateDTO) {
    try {
      int result = componentService.insertQnaCate(qnaCateDTO);

      if (result == 409) {
        return ResponseEntity.status(handler.getHttpStatus(409))
                .body(handler.createResponse(409, "이미 존재하는 QNA 카테고리 이름입니다."));
      }

      return ResponseEntity.status(handler.getHttpStatus(201))
              .body(handler.createResponse(201, "QNA 카테고리 등록 성공"));

    } catch (Exception e) {
      log.error("FAQ 카테고리 등록 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "QNA 카테고리 등록 중 서버 오류가 발생했습니다."));
    }
  }

  // QNA 카테고리 수정
  @PatchMapping("/qnaCategory/{qnaCategoryIdx}")
  public ResponseEntity<Map<String, Object>> updateQnaCate(@PathVariable("qnaCategoryIdx")int qnaCategoryIdx,
                                                           @RequestBody QnaCateDTO qnaCateDTO){
    try {
      qnaCateDTO.setQnaCategoryIdx(qnaCategoryIdx);
      int result = componentService.updateQnaCate(qnaCateDTO);

      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "QNA 카테고리 수정 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "수정할 QNA 카테고리를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("QNA 카테고리 수정 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // QNA 카테고리 삭제
  @DeleteMapping("qnaCategory/{qnaCategoryIdx}")
  public ResponseEntity<Map<String, Object>> deleteQnaCate(@PathVariable("qnaCategoryIdx") int qnaCategoryIdx){
    try {
      int result = componentService.deleteQnaCate(qnaCategoryIdx);
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "QNA 카테고리 삭제 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "삭제할 QNA 카테고리를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("QNA 카테고리 삭제 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }
}


