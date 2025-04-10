package com.godLife.project.controller;


import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

  private final GlobalExceptionHandler handler;

  private final QnaService qnaService;



  // 1:1 문의 작성
  @PostMapping("/auth/create")
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


}
