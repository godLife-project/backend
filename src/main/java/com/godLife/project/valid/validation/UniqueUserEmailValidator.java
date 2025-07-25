package com.godLife.project.valid.validation;

import com.godLife.project.mapper.UserMapper;
import com.godLife.project.valid.annotation.UniqueUserEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

  private final UserMapper userMapper;

  public UniqueUserEmailValidator(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public boolean isValid(String userEmail, ConstraintValidatorContext context) {
    if (userEmail == null || userEmail.trim().isEmpty()) {
      return false;
    }
    return !userMapper.checkUserEmailExist(userEmail);
  }
}
