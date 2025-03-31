package com.godLife.project.dto.request.myPage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ModifyPersonalRequestDTO {
  @Schema(description = "유저 idx", example = "1")
  private int userIdx;

  @Schema(description = "유저 이름", example = "홍길동")
  @NotBlank(message = "{joinUser.userName.notBlank}")
  @Size(min=3, max=15, message = "{joinUser.userName.size}")
  private String userName;

  @Schema(description = "유저 전화번호", example = "010-1234-5678")
  @Pattern(
      regexp = "^(010-\\d{4}-\\d{4})|(02-\\d{3,4}-\\d{4})|([0-9]{3}-\\d{3,4}-\\d{4})$",
      message = "{joinUser.userPhone.pattern}"
  )
  @NotBlank(message = "{joinUser.userPhone.notBlank}")
  private String userPhone;

  @Schema(description = "유저 성별", example = "1")
  @Min(value = 1, message = "{joinUser.userGender.min}")
  private int userGender;
}
