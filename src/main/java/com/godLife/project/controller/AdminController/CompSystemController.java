package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.categories.FaqCateDTO;
import com.godLife.project.dto.categories.QnaCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.CompSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/admin/compSystem")
public class CompSystemController {
  @Autowired
  private GlobalExceptionHandler handler;

  @Autowired
  private final CompSystemService compSystemService;


  public CompSystemController(CompSystemService compSystemService) {
    this.compSystemService = compSystemService;
  }

  //                                  FAQ 카테고리 관리 테이블
  // FAQ 카테고리 조회
  @GetMapping("/faqCategory")
  public ResponseEntity<Map<String, Object>> selectFaqCate() {
    try {
      List<FaqCateDTO> faqCateDTOList = compSystemService.selectFaqCate();

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
      int result = compSystemService.insertFaqCate(faqCateDTO);

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
      int result = compSystemService.updateFaqCate(faqCateDTO);

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
      int result = compSystemService.deleteFaqCate(faqCategoryIdx);
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
      List<QnaCateDTO> qnaCateDTOList = compSystemService.selectQnaCate();

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
      int result = compSystemService.insertQnaCate(qnaCateDTO);

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
      qnaCateDTO.setCategoryIdx(qnaCategoryIdx);
      int result = compSystemService.updateQnaCate(qnaCateDTO);

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
      int result = compSystemService.deleteQnaCate(qnaCategoryIdx);
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


  //                                  Top Menu 관리 테이블
  // Top Menu  조회
  @GetMapping("/topMenu")
  public ResponseEntity<Map<String, Object>> selectTopMenu() {
    try {
      List<TopCateDTO> TopMenuList = compSystemService.selectTopMenu();

      if (TopMenuList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "등록된 TopMenu 가 없습니다."));
      }

      Map<String, Object> response = handler.createResponse(200, "TopMenu 조회 성공");
      response.put("TopMenu", TopMenuList);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("TopMenu 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 TopMenu 조회에 실패했습니다."));
    }
  }

  // TopMenu 추가
  @PostMapping("/topMenu")
  public ResponseEntity<Map<String, Object>> insertTopMenu(@RequestBody TopCateDTO topCateDTO) {
    try {
      int result = compSystemService.insertTopMenu(topCateDTO);

      if (result == 409) {
        return ResponseEntity.status(handler.getHttpStatus(409))
                .body(handler.createResponse(409, "이미 존재하는 TopMenu 이름입니다."));
      }

      return ResponseEntity.status(handler.getHttpStatus(201))
              .body(handler.createResponse(201, "TopMenu 등록 성공"));

    } catch (Exception e) {
      log.error("TopMenu 등록 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "TopMenu 등록 중 서버 오류가 발생했습니다."));
    }
  }

  // TopMenu 수정
  @PatchMapping("/topMenu/{topIdx}")
  public ResponseEntity<Map<String, Object>> updateTopMenu(@PathVariable("topIdx")int topIdx,
                                                           @RequestBody TopCateDTO topCateDTO){
    try {
      topCateDTO.setTopIdx(topIdx);
      int result = compSystemService.updateTopMenu(topCateDTO);

      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "TopMenu 수정 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "수정할 TopMenu를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("TopMenu 수정 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // TopMenu 삭제
  @DeleteMapping("topMenu/{topIdx}")
  public ResponseEntity<Map<String, Object>> deleteTopMenu(@PathVariable("topIdx") int topIdx){
    try {
      int result = compSystemService.deleteTopMenu(topIdx);
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "TopMenu 삭제 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "삭제할 TopMenu를 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("TopMenu 삭제 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // 드래그 앤 드롭으로 정렬 순서 업데이트
  @PutMapping("/topMenu/reorder")
  public ResponseEntity<Map<String, Object>> reorderTopMenu(@RequestBody List<TopCateDTO> orderedList) {
    try {
      compSystemService.updateOrderTopMenu(orderedList);
      return ResponseEntity.ok(handler.createResponse(200, "TopMenu 순서 업데이트 성공"));
    } catch (Exception e) {
      log.error("TopMenu 순서 업데이트 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
}

  //                                  ICON 관리 테이블
  // ICON 조회
  @GetMapping("/icon")
  public ResponseEntity<Map<String, Object>> selectIcon() {
    try {
      List<IconDTO> IconDTOList = compSystemService.selectIcon();

      if (IconDTOList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "등록된 ICON 이 없습니다."));
      }

      Map<String, Object> response = handler.createResponse(200, "ICON 조회 성공");
      response.put("ICON", IconDTOList);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("ICON 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 ICON 조회에 실패했습니다."));
    }
  }

  // ICON 추가
  @PostMapping("/icon")
  public ResponseEntity<Map<String, Object>> insertIcon(@RequestBody IconDTO iconDTO) {
    try {
      int result = compSystemService.insertIcon(iconDTO);

      if (result == 409) {
        return ResponseEntity.status(handler.getHttpStatus(409))
                .body(handler.createResponse(409, "이미 존재하는 ICON 이름입니다."));
      }

      return ResponseEntity.status(handler.getHttpStatus(201))
              .body(handler.createResponse(201, "ICON 등록 성공"));

    } catch (Exception e) {
      log.error("ICON 등록 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "ICON 등록 중 서버 오류가 발생했습니다."));
    }
  }

  // ICON 수정
  @PatchMapping("/icon/{iconKey}")
  public ResponseEntity<Map<String, Object>> updateIcon(@PathVariable("iconKey")String iconKey,
                                                           @RequestBody IconDTO iconDTO){
    try {
      iconDTO.setIconKey(iconKey);
      int result = compSystemService.updateIcon(iconDTO);

      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "ICON 수정 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "수정할 ICON을 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("ICON 수정 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }

  // ICON 삭제
  @DeleteMapping("icon/{iconKey}")
  public ResponseEntity<Map<String, Object>> deleteIcon(@PathVariable("iconKey") String iconKey){
    try {
      int result = compSystemService.deleteIcon(iconKey);
      if (result > 0) {
        return ResponseEntity.ok(handler.createResponse(200, "ICON 삭제 성공"));
      } else {
        return ResponseEntity.status(handler.getHttpStatus(404))
                .body(handler.createResponse(404, "삭제할 ICON을 찾을 수 없습니다."));
      }
    } catch (Exception e) {
      log.error("ICON 삭제 오류: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류 발생"));
    }
  }
}
