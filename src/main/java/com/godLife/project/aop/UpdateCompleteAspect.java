package com.godLife.project.aop;

import com.godLife.project.mapper.PlanMapper;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@EnableAspectJAutoProxy
@Aspect
@Component
public class UpdateCompleteAspect {

  /*
  private final PlanMapper planMapper;

  public UpdateCompleteAspect(PlanMapper planMapper) {
    this.planMapper = planMapper;
  }

  // CategoryController의 /topMenu 요청 전에만 실행
  @Before("execution(* com.godLife.project.controller.CategoryController.topMenu(..))")
  public void updateAllCompleteBeforeRequest() {
    //System.out.println("루틴 완료 처리 로직 활성화..");
    planMapper.updateAllComplete();
  }

   */
}

