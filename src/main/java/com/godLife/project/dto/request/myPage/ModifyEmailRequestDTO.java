package com.godLife.project.dto.request.myPage;

import com.godLife.project.valid.annotation.UniqueUserEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ModifyEmailRequestDTO {
  @Schema(description = "유저 idx", example = "1")
  private int userIdx;

  @Schema(description = "유저 이메일", example = "hong@example.com")
  @Email(message = "{joinUser.userEmail.email}")
  @UniqueUserEmail
  private String userEmail;
}
