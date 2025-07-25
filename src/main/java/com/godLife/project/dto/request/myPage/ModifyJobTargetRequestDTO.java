package com.godLife.project.dto.request.myPage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ModifyJobTargetRequestDTO {

  private int userIdx;

  @Schema(description = "현재 직업 idx", example = "1")
  @Min(value = 1, message = "{joinUser.jobIdx.min}")
  private int jobIdx;

  @Schema(description = "초기 관심사 idx", example = "1")
  @Min(value = 1, message = "{joinUser.targetIdx.min}")
  private int targetIdx;
}
