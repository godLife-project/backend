package com.godLife.project.dto.request;

import com.godLife.project.valid.annotation.CheckUserEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class GetEmailRequestDTO {
  @Schema(description = "유저 이메일", example = "hong@example.com")
  @Email(message = "{joinUser.userEmail.email}")
  @CheckUserEmail
  private String userEmail;
}
