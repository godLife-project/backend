package com.godLife.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyPageUserInfosResponseDTO {
  // 아이디
  private String userId;
  // 이메일
  private String userEmail;
  // 가입일
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime userJoin;
  // 전화번호
  private String userPhone;
  // 성별
  private int userGender;

  // 직업
  private JobCateDTO jobInfos;
  // 목표
  private TargetCateDTO targetInfos;


}
