package com.godLife.project.valid.annotation;

import com.godLife.project.valid.validation.UniqueUserIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUserIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserId {
  String message() default "아이디가 중복되는지 확인해주세요.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
