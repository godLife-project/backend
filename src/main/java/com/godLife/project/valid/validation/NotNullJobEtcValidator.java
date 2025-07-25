package com.godLife.project.valid.validation;

import com.godLife.project.dto.categories.JobEtcCateDTO;
import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.valid.annotation.NotNullJobEtc;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullJobEtcValidator implements ConstraintValidator<NotNullJobEtc, PlanDTO> {

  @Override
  public boolean isValid(PlanDTO planDTO, ConstraintValidatorContext context) {
    // jobIdx가 999가 아닐 경우 유효성 검사 통과
    if (planDTO.getJobIdx() != 999) {
      return true;
    }
    JobEtcCateDTO jobEtc = planDTO.getJobEtcCateDTO();

    // jobEtcCateDTO가 null이면 유효성 검사 실패
    if (jobEtc == null) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{writePlan.jobEtc.notNull}")
          .addPropertyNode("jobEtcCateDTO")
          .addConstraintViolation();
      return false;
    }

    boolean isValid = true;

    // name 필드가 비어 있으면 에러 추가
    if (jobEtc.getName() == null || jobEtc.getName().trim().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{writePlan.jobEtc.name.notBlank}")
          .addPropertyNode("jobEtcCateDTO.name")
          .addConstraintViolation();
      isValid = false;
    }

    // iconKey 필드가 비어 있으면 에러 추가
    if (jobEtc.getIconKey() == null || jobEtc.getIconKey().trim().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("{writePlan.jobEtc.iconKey.notBlank}")
          .addPropertyNode("jobEtcCateDTO.iconKey")
          .addConstraintViolation();
      isValid = false;
    }

    return isValid;
  }
}
