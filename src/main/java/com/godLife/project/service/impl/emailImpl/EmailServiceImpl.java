package com.godLife.project.service.impl.emailImpl;

import com.godLife.project.dto.email.MailTxtSendDTO;
import com.godLife.project.service.interfaces.emailInterface.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String EMAIL_SENDER;

  @Override
  public void sendEmail(String toEmail,
                        String title,
                        String text) {
    SimpleMailMessage email = createEmailForm(toEmail, title, text);
    try {
      mailSender.send(email);                   // 메일 보내기
      log.info("이메일 전송 성공!");
    } catch (MailException e) {
      log.error("[-] 이메일 전송중에 오류가 발생하였습니다 \n{}", e.getMessage());
      throw e;
    }
  }

  // 발신할 이메일 세팅
  private SimpleMailMessage createEmailForm(String toEmail,
                                            String title,
                                            String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);         // 받는 사람 이메일
    message.setFrom(EMAIL_SENDER);  // 보내는 사람 추가
    message.setSubject(title);      // 이메일 제목
    message.setText(text);          // 이메일 내용

    return message;
  }


}
