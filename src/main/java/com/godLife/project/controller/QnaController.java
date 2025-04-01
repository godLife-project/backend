package com.godLife.project.controller;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.dto.contents.QnaContentDTO;
import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.QnaService;
import com.godLife.project.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/qna")
public class QnaController {
  private final QnaService qnaService;

  private UserService userService;  // UserService를 통해 사용자 정보 조회

  @Autowired
  private GlobalExceptionHandler handler;


  public QnaController(QnaService qnaService, UserService userService) {
    this.qnaService = qnaService;
    this.userService = userService;}


  // 1:1 문의 시작: 사용자가 카테고리와 첫 번째 질문을 보냄
  @PostMapping("/auth/startChat")
  public ResponseEntity<String> startChat(@RequestBody QnaContentDTO qnaContentDTO, Principal principal) {
    String userId = principal.getName();  // 로그인한 사용자의 userId 가져오기

    // userId를 통해 userIdx를 조회
    UserDTO userDTO = userService.findByUserId(userId);  // UserService에서 userId로 UserDTO 조회
    Long userIdx = (long) userDTO.getUserIdx();  // 유저 인덱스를 가져옴

    // 첫 번째 질문을 QNA_TABLE에 저장
    QnADTO qnaDTO = new QnADTO();
    qnaDTO.setQIdx(userIdx);  // 로그인된 사용자 인덱스
    qnaDTO.setQCategory(qnaContentDTO.getQuestionIdx());  // 선택한 카테고리
    qnaDTO.setQTitle(qnaContentDTO.getResponseSub());  // 첫 번째 질문 내용
    qnaService.insertQuestion(qnaDTO);  // QNA_TABLE에 첫 번째 질문 추가

    // 첫 번째 메시지 내용을 QNA_CONTENT에 저장
    qnaContentDTO.setResponseIdx(userIdx);  // 사용자의 인덱스
    qnaService.insertQnaContent(qnaContentDTO);  // QNA_CONTENT에 첫 번째 메시지 저장

    // 성공 시 메시지 반환
    return ResponseEntity.ok("정상적으로 문의가 되었습니다");
  }

  // 답변 추가: 사용자 또는 관리자가 응답
  @PostMapping("/auth/respond")
  public ResponseEntity<String> respondToQuestion(@RequestBody QnaContentDTO qnaContentDTO, Principal principal) {
    String userId = principal.getName();  // 로그인한 사용자의 userId 가져오기

    // userId를 통해 userIdx를 조회
    UserDTO userDTO = userService.findByUserId(userId);  // UserService에서 userId로 UserDTO 조회
    Long userIdx = (long) userDTO.getUserIdx();  // 유저 인덱스를 가져옴
    qnaContentDTO.setResponseIdx(userIdx);  // 응답자 인덱스 설정

    if (userDTO.isRoleStatus()) {  // 관리자 여부 확인
      // 관리자 응답 로직
      qnaService.insertQnaContent(qnaContentDTO);  // QNA_CONTENT에 답변 저장
      return ResponseEntity.ok("관리자가 정상적으로 응답하였습니다.");
    } else {
      // 사용자 응답 로직
      qnaService.insertQnaContent(qnaContentDTO);  // QNA_CONTENT에 답변 저장
      return ResponseEntity.ok("사용자가 정상적으로 응답하였습니다.");
    }
  }

  // 특정 qnaIdx에 해당하는 메시지 목록 조회
  @GetMapping("/auth/{qnaIdx}/messages")
  public ResponseEntity<List<QnaContentDTO>> getMessages(@PathVariable Long qnaIdx) {
    List<QnaContentDTO> messages = qnaService.getQnaContent(qnaIdx);  // QNA_CONTENT 테이블에서 메시지 목록 조회
    return ResponseEntity.ok(messages);  // 메시지 목록 반환
  }

}
