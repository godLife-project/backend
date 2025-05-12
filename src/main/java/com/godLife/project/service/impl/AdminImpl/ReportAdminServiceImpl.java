package com.godLife.project.service.impl.AdminImpl;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import com.godLife.project.mapper.AdminMapper.ReportAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.ReportAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportAdminServiceImpl implements ReportAdminService {

  private ReportAdminMapper reportAdminMapper;

  public ReportAdminServiceImpl(ReportAdminMapper reportAdminMapper) {
    this.reportAdminMapper = reportAdminMapper;
  }

  // 유저 신고 ALL 조회
  public List<UserReportDTO> getAllReports(int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.getAllReports(offset, size);
  }

  // 유저신고 처리상태별 조회
  public List<UserReportDTO> getReportsByStatus(int status, int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.selectReportsByStatus(status, offset, size);
  }

  // 유저 신고 총 개수
  public int countAllReports() {
    return reportAdminMapper.countAllReports();
  }

  // 유저 신고상태별 총 개수
  public int countReportsByStatus(int status) {
    return reportAdminMapper.countReportsByStatus(status);
  }

  // 유저신고 처리 로직
  @Transactional
  public void userReportStateUpdate(UserReportDTO userReportDTO) {
    // 신고 상태 업데이트
    reportAdminMapper.userReportStateUpdate(userReportDTO);

    // status가 1(처리 완료)일 때만 누적 로직 실행
    if (userReportDTO.getStatus() == 1) {
      // 해당 신고글의 REPORTED_IDX 가져오기
      Integer reportedIdx = reportAdminMapper.findReportedIdxByReportIdx(userReportDTO.getUserReportIdx());
      if (reportedIdx == null) {
        throw new IllegalArgumentException("REPORTED_IDX를 찾을 수 없습니다");
      }

      // 신고당한 유저의 신고 횟수 증가
      reportAdminMapper.incrementReportCount(reportedIdx);

      // 정지 조건 검사 및 적용
      Integer reportCount = reportAdminMapper.getReportCount(reportedIdx);
      if (reportCount != null && reportCount >= 5) {
        reportAdminMapper.banUser(reportedIdx);
      }
    }
  }


  // 루틴신고 ALL 조회
  public List<PlanReportDTO> getAllPlanReports(int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.selectPlanReports(offset, size);
  }
  // 루틴신고 처리상태별 조회
  public List<PlanReportDTO> getPlanReportsByStatus(int status, int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.selectPlanReportsByStatus(status, offset, size);
  }
  // 루틴신고 총 개수
  public int countAllPlanReports() {
    return reportAdminMapper.countAllPlanReports();
  }

  @Transactional
  public void planReportStateUpdate(PlanReportDTO planReportDTO) {
    // 신고 상태 업데이트
    reportAdminMapper.planReportStateUpdate(planReportDTO);
  }


}

