package com.godLife.project.dto.request.myPage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ModifyNicknameRequestDTO {
  @Schema(description = "유저 idx", example = "1")
  private int userIdx;

  @Schema(description = "유저 닉네임", example = "의적단")
  @NotBlank(message = "{joinUser.userNick.notBlank}")
  @Size(min=2, max=15, message = "{joinUser.userNick.size}")
  @Pattern(regexp="[가-힣a-zA-Z0-9-_]*", message = "{joinUser.userNick.pattern}")
  private String userNick;

  @Schema(description = "닉네임 중복 태그", example = "#1")
  @Pattern(regexp="#[가-힣a-zA-Z0-9-_]+", message = "{joinUser.nickTag.pattern}")
  private String nickTag;
}
