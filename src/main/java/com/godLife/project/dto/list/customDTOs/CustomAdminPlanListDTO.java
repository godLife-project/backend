package com.godLife.project.dto.list.customDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomAdminPlanListDTO {

  private int planIdx;
  private int userIdx;  // USER_IDX 컬럼 매핑
  private String planTitle;
  private Integer targetIdx;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime planSubDate;
}
