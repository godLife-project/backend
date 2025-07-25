package com.godLife.project.valid.annotation;

import com.godLife.project.valid.validation.CheckUserEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckUserEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserEmail {
  String message() default "등록된 이메일이 아닙니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
