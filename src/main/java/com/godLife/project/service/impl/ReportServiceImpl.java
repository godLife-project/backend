package com.godLife.project.service.impl;


import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import com.godLife.project.mapper.ReportMapper;
import com.godLife.project.service.interfaces.jwtInterface.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportMapper reportMapper;

  // 에러 코드 상수화
  private static final String CHECK_CONSTRAINT_VIOLATION = "ORA-02290";
  private static final String NOT_NULL_VIOLATION = "ORA-01400";
  private static final String FOREIGN_KEY_VIOLATION = "ORA-02291";

  // 루틴 신고
  @Override
  public int planReport(PlanReportDTO planReportDTO) {
    try {
      reportMapper.planReport(planReportDTO);
      return 200;

    } catch (DuplicateKeyException e) {
      int status = reportMapper.getStatusAtPlanReport(planReportDTO);
      if (status == 3) {
        reportMapper.rePlanReport(planReportDTO);
        return 200;
      }

      return 409;

    }catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException sqlEx) {
        String errorMessage = sqlEx.getMessage();
        if (errorMessage.contains(NOT_NULL_VIOLATION)) {
          System.out.println("not null 조건 위반!");
          return 422;
        }
        if (errorMessage.contains(FOREIGN_KEY_VIOLATION)) {
          System.out.println("foreign key 조건 위반!");
          return 404;
        }
      }
      return 500;

    } catch (Exception e) {
      log.error("e: ", e);
      return 500;
    }
  }

  // 루틴 신고 취소
  @Override
  public int planReportCancel(PlanReportDTO planReportDTO) {
    int result = reportMapper.planReportCancel(planReportDTO);
    if (result == 0) { return 404; }
    return 200;
  }

  // 유저 신고
  @Override
  public int userReport(UserReportDTO userReportDTO) {
    try {
      reportMapper.userReport(userReportDTO);
      return 200;

    } catch (DuplicateKeyException e) {
      int status = reportMapper.getStatusAtUserReport(userReportDTO);
      if (status == 3) {
        reportMapper.reUserReport(userReportDTO);
        return 200;
      }

      return 409;

    } catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException sqlEx) {
        String errorMessage = sqlEx.getMessage();
        if (errorMessage.contains(CHECK_CONSTRAINT_VIOLATION)) {
          System.out.println("체크 제약 조건 위반!");
          return 400;
        }
        if (errorMessage.contains(NOT_NULL_VIOLATION)) {
          System.out.println("not null 조건 위반!");
          return 422;
        }
        if (errorMessage.contains(FOREIGN_KEY_VIOLATION)) {
          System.out.println("foreign key 조건 위반!");
          return 404;
        }
      }
      return 500;

    } catch (Exception e) {
      log.error("e: ", e);
      return 500;
    }
  }

  // 유저 신고 취소
  @Override
  public int userReportCancel(UserReportDTO userReportDTO) {
    int result = reportMapper.userReportCancel(userReportDTO);
    if (result == 0) { return 404; }
    return 200;
  }
}
