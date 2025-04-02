package com.godLife.project.dto.request;

import com.godLife.project.valid.annotation.CheckUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GetNameNEmail {
  @NotBlank(message = "{joinUser.userName.notBlank}")
  @Size(min=3, max=15, message = "{joinUser.userName.size}")
  private String userName;

  @Email(message = "{joinUser.userEmail.email}")
  @NotBlank(message = "{joinUser.userEmail.notBlank}")
  @CheckUserEmail
  private String userEmail;
}
