package com.godLife.project.controller;


import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.contents.QnaReplyDTO;
import com.godLife.project.enums.QnaStatus;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qna/auth")
@RequiredArgsConstructor
public class QnaController {

  private final GlobalExceptionHandler handler;

  private final QnaService qnaService;



  // 1:1 문의 작성
  @PostMapping("/create")
  public ResponseEntity<Map<String, Object>> createQna(@RequestHeader("Authorization") String authHeader,
                                                       @Valid @RequestBody QnaDTO writeQna,
                                                       BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    writeQna.setQUserIdx(userIdx);

    qnaService.createQna(writeQna);

    // 응답 메시지 설정
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  // 1:1 문의 본문 조회
  @GetMapping("/get/just/content/{qnaIdx}")
  public ResponseEntity<Map<String, Object>> getJustQnaContent(@PathVariable int qnaIdx) {
    String content = qnaService.getQnaContent(qnaIdx);

    return ResponseEntity.ok().body(handler.createResponse(200, content));
  }

  // 1:1 문의 답변 달기
  @PostMapping("/comment/reply")
  public ResponseEntity<Map<String, Object>> commentReply(@RequestHeader("Authorization") String authHeader,
                                                          @Valid @RequestBody QnaReplyDTO qnaReplyDTO,
                                                          BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    qnaReplyDTO.setUserIdx(userIdx);

    qnaService.commentReply(qnaReplyDTO);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  // 1:1 문의 수정
  @PatchMapping("/modify")
  public ResponseEntity<Map<String, Object>> modifyQnA(@RequestHeader("Authorization") String authHeader,
                                                       @Valid @RequestBody QnaDTO modifyQna,
                                                       BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    modifyQna.setQUserIdx(userIdx);

    List<String> setStatus = new ArrayList<>();
    setStatus.add(QnaStatus.WAIT.getStatus());
    setStatus.add(QnaStatus.CONNECT.getStatus());

    // 문의 수정
    qnaService.modifyQnA(modifyQna, setStatus);

    return ResponseEntity.ok().body(handler.createResponse(200, "문의 수정 완료"));
  }

  // 1:1 답변 수정
  @PatchMapping("/modify/reply")
  public ResponseEntity<Map<String, Object>> modifyReply(@RequestHeader("Authorization") String authHeader,
                                                         @Valid @RequestBody QnaReplyDTO modifyReplyDTO,
                                                         BindingResult valid) {
    if (valid.hasErrors()) {
      return ResponseEntity.badRequest().body(handler.getValidationErrors(valid));
    }

    int userIdx = handler.getUserIdxFromToken(authHeader);
    modifyReplyDTO.setUserIdx(userIdx);

    qnaService.modifyReply(modifyReplyDTO);

    return ResponseEntity.ok().body(handler.createResponse(200, "답변 수정 완료"));
  }

  // 문의 삭제
  @DeleteMapping("/delete/{qnaIdx}")
  ResponseEntity<Map<String, Object>> deleteQnA(@RequestHeader("Authorization") String authHeader,
                                                @PathVariable int qnaIdx) {
    int userIdx = handler.getUserIdxFromToken(authHeader);

    qnaService.deleteQna(qnaIdx, userIdx);

    return ResponseEntity.ok().body(handler.createResponse(200, "문의가 정상적으로 삭제 되었습니다."));
  }

  // 답변 삭제
  @DeleteMapping("/delete/reply/{qnaIdx}")
  ResponseEntity<Map<String, Object>> deleteReply(@RequestHeader("Authorization") String authHeader,
                                                  @PathVariable int qnaIdx,
                                                  @RequestParam int qnaReplyIdx) {
    int userIdx = handler.getUserIdxFromToken(authHeader);

    qnaService.deleteReply(qnaIdx, qnaReplyIdx, userIdx);

    return ResponseEntity.ok().body(handler.createResponse(200, "답변이 정상적으로 삭제 되었습니다."));
  }


}
