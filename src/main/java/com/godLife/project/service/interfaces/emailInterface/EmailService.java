package com.godLife.project.service.interfaces.emailInterface;

import com.godLife.project.dto.email.MailTxtSendDTO;

public interface EmailService {
  // SimpleMailMessage를 활용하여 텍스트 기반 메일을 전송합니다.
  void sendEmail(String toEmail, String title, String text);

}
