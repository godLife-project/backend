package com.godLife.project.dto.list.customDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomPlanListDTO {

  private int rank;
  private int planIdx;
  private String userNick;
  private String planTitle;
  @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime planSubDate;
  private int viewCount;
  private int likeCount;
  private int forkCount;
  private int isActive;
  private int isCompleted;
  private int isShared;
}
