package com.godLife.project.valid.annotation;

import com.godLife.project.valid.validation.NotNullJobEtcValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotNullJobEtcValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullJobEtc {
  String message() default "'기타' 선택 시 직업명과 아이콘을 필수로 선택해주세요.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
