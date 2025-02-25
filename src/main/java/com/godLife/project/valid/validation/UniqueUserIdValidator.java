package com.godLife.project.valid.validation;

import com.godLife.project.mapper.UserMapper;
import com.godLife.project.valid.annotation.UniqueUserId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueUserIdValidator implements ConstraintValidator<UniqueUserId, String> {

  private final UserMapper userMapper;

  public UniqueUserIdValidator(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public boolean isValid(String userId, ConstraintValidatorContext context) {
    if (userId == null || userId.trim().isEmpty()) {
      return false;
    }
    return !userMapper.checkUserIdExist(userId);
  }
}
