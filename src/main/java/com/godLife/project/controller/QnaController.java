package com.godLife.project.controller;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.service.QnaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/qna")
public class QnaController {
  private final QnaService qnaService;

  public QnaController(QnaService qnaService) {
    this.qnaService = qnaService;
  }

  // 특정 QnA 조회
  @GetMapping("/{qnaIdx}")
  public ResponseEntity<QnADTO> getQnaById(@PathVariable int qnaIdx) {
    QnADTO qna = qnaService.getQnaById(qnaIdx);
    return ResponseEntity.ok(qna);
  }

  // 모든 QnA 조회
  @GetMapping
  public ResponseEntity<List<QnADTO>> getAllQna() {
    List<QnADTO> qnaList = qnaService.selectAllQna();
    return ResponseEntity.ok(qnaList);
  }

  // QnA 작성
  @PostMapping
  public ResponseEntity<String> createQna(@RequestBody QnADTO qna) {
    qnaService.createQna(qna);
    return ResponseEntity.ok("QnA 등록 성공");
  }

  // QnA 수정
  @PutMapping("/{qnaIdx}")
  public ResponseEntity<String> updateQna(@PathVariable int qnaIdx, @RequestBody QnADTO qna) {
    qna.setQnaIdx(qnaIdx);
    qnaService.updateQna(qna);
    return ResponseEntity.ok("QnA 수정 성공");
  }

  // QnA 삭제
  @DeleteMapping("/{qnaIdx}")
  public ResponseEntity<String> deleteQna(@PathVariable int qnaIdx) {
    qnaService.deleteQna(qnaIdx);
    return ResponseEntity.ok("QnA 삭제 성공");
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
}
