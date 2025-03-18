package com.godLife.project.service.impl;


import com.godLife.project.dto.infos.PlanReportDTO;
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
       return 404;
     } catch (Exception e) {
       log.error("e: ", e);
       return 500;
     }
   }
  @Override
  public int planReportCancel(PlanReportDTO planReportDTO) {
    int result = reportMapper.planReportCancel(planReportDTO);
    if (result == 0) { return 404; }
    return 200;
  }
}
