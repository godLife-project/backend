package com.godLife.project.controller.emailCheck;

import com.godLife.project.dto.email.MailTxtSendDTO;
import com.godLife.project.service.interfaces.emailInterface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService emailService;
/*
  // 1️⃣ 이메일 인증 코드 전송 API
  @PostMapping("/send")
  public String sendVerificationCode(@RequestBody MailTxtSendDTO email) {
    String authCode = emailService.generateAuthCode();
    email.setSubject("[갓생로그] 이메일 인증 코드 입니다.");
    email.setContent("인증코드 :: " + authCode);
    emailService.sendTxtEmail(email);

    return "인증 코드가 이메일로 전송되었습니다.";
  }
  */
}
