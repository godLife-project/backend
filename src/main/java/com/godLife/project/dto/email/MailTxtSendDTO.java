package com.godLife.project.dto.email;

import lombok.Data;

@Data
public class MailTxtSendDTO {

  private String emailAddr;                   // 수신자 이메일

  private String subject;                     // 이메일 제목

  private String content;                     // 이메일 내용
}
