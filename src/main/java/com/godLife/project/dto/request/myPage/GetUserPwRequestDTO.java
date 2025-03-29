package com.godLife.project.dto.request.myPage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GetUserPwRequestDTO {
  @Schema(description = "유저 비밀번호", example = "1234")
  @NotBlank(message = "{joinUser.userPw.notBlank}")
  @Size(min = 4, max = 15, message = "{joinUser.userPw.size}")
  @Pattern(regexp="[a-zA-Z0-9]*", message = "{joinUser.userPw.pattern}")
  private String userPw;

  private String userPwConfirm;
}
