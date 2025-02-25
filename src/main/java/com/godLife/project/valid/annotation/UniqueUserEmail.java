package com.godLife.project.valid.annotation;

import com.godLife.project.valid.validation.UniqueUserEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUserEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserEmail {
  String message() default "이메일이 중복되는지 확인해주세요.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
