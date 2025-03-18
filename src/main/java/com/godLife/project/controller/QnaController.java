package com.godLife.project.controller;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qna")
public class QnaController {
  private final QnaService qnaService;
  @Autowired
  private GlobalExceptionHandler handler;

  public QnaController(QnaService qnaService) {
    this.qnaService = qnaService;
  }

  // 특정 QnA 조회
  @GetMapping("/{qnaIdx}")
  public ResponseEntity<Map<String, Object>> getQnaById(@PathVariable int qnaIdx, BindingResult result) {
    // 첫 번째 호출로 이미 qna 데이터를 얻었으므로 두 번째 호출은 불필요
    QnADTO qna = qnaService.getQnaById(qnaIdx);

    // 검증 오류가 있을 경우 처리
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(result));
    }

    // qna가 null인 경우를 체크하여 처리 (선택적)
    if (qna == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(handler.createResponse(404, "QnA not found"));
    }

    // 정상적인 경우 응답 반환
    return ResponseEntity.ok(handler.createResponse(200, qna));
  }

  // 모든 QnA 조회
  @GetMapping
  public ResponseEntity<Map<String, Object>> getAllQna() {
    List<QnADTO> qnaList = qnaService.selectAllQna();

    // QnA 게시글이 없을 경우 404 상태 코드와 메시지 반환
    if (qnaList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(handler.createResponse(404, "게시글이 존재하지 않습니다."));
    }

    return ResponseEntity.ok(handler.createResponse(200, qnaList));
  }


  // QnA 작성
  @PostMapping
  public ResponseEntity<String> createQna(@RequestBody QnADTO qna) {
    int result = qnaService.createQna(qna);
    String msg;
    switch (result) {
      case 200 -> msg = "QnA 등록 성공";
      case 500 -> msg = "서버 오류로 인해 QnA 등록 실패";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }
    return ResponseEntity.status(result).body(msg);
  }


  // QnA 수정 API
  @PutMapping("/{qnaIdx}")
  public ResponseEntity<String> updateQna(@PathVariable int qnaIdx, @RequestBody QnADTO qnaDTO) {
    // qnaIdx를 DTO에 set하여 전달
    qnaDTO.setQnaIdx(qnaIdx);

    // 서비스 호출
    int result = qnaService.updateQna(qnaDTO);

    // 상태 코드 및 메시지 반환
    switch (result) {
      case 200:
        return ResponseEntity.ok("QnA 수정 성공");
      case 403:
        return ResponseEntity.status(403).body("작성자가 아니므로 수정할 수 없습니다.");
      case 500:
        return ResponseEntity.status(500).body("서버 오류로 인해 QnA 수정에 실패했습니다.");
      default:
        return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다.");
    }
  }

  // QnA 삭제 API
  @DeleteMapping("/{qnaIdx}")
  public ResponseEntity<String> deleteQna(@PathVariable int qnaIdx, @PathVariable int userIdx) {
    // 서비스 호출
    int result = qnaService.deleteQna(qnaIdx, userIdx);

    // 상태 코드 및 메시지 반환
    switch (result) {
      case 200:
        return ResponseEntity.ok("QnA 삭제 성공");
      case 403:
        return ResponseEntity.status(403).body("작성자가 아니므로 삭제할 수 없습니다.");
      case 500:
        return ResponseEntity.status(500).body("서버 오류로 인해 QnA 삭제에 실패했습니다.");
      default:
        return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다.");
    }
  }

  // QnA 검색
  @GetMapping("/search")
  public ResponseEntity<List<QnADTO>> searchQna(@RequestParam(value = "query", required = false) String query) {
    try {
      List<QnADTO> searchResult = qnaService.searchQna(query != null ? query : "");
      return ResponseEntity.ok(searchResult);
    } catch (Exception e) {
      // 예외 처리 후 내부 서버 오류 반환
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Collections.emptyList());
    }
  }

  @PostMapping("/answer/{qnaIdx}")
  public ResponseEntity<String> saveAnswer(@PathVariable int qnaIdx,
                                           @RequestBody QnADTO qnADTO) {
    // 답변 등록 API
    qnaService.saveAnswer(qnaIdx, qnADTO.getAIdx(), qnADTO.getASub());
    return ResponseEntity.ok("답변 등록 완료");
  }
}
