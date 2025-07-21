package com.godLife.project.service.impl.adminImpl;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import com.godLife.project.mapper.AdminMapper.ReportAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.ReportAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportAdminServiceImpl implements ReportAdminService {

  private final ReportAdminMapper reportAdminMapper;


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

  // 전체 루틴 신고 수
  public int countAllPlanReports() {
    return reportAdminMapper.countAllPlanReports(); // ➜ Mapper에 정의 필요
  }

  // 상태별 루틴 신고 수
  public int countPlanReportsByStatus(int status) {
    return reportAdminMapper.countPlanReportsByStatus(status);
  }

  // 전체 루틴 신고 리스트
  public List<PlanReportDTO> getAllPlanReports(int offset, int limit) {
    return reportAdminMapper.selectPlanReports(offset, limit);
  }

  // 상태별 루틴 신고 리스트
  public List<PlanReportDTO> getPlanReportsByStatus(int status, int offset, int limit) {
    return reportAdminMapper.selectPlanReportsByStatus(status, offset, limit);
  }


  // 루틴 처리 로직
  public int planReportStateUpdate(PlanReportDTO planReportDTO, int userIdx) {

    Integer expectedPlanIdx = reportAdminMapper.getPlanIdxByReportIdx(planReportDTO.getPlanReportIdx());

    if (expectedPlanIdx == null || !expectedPlanIdx.equals(planReportDTO.getPlanIdx())) {
      return 400; // Bad Request
    }

    try {
      reportAdminMapper.planReportStateUpdate(planReportDTO);
      planReportDTO.setIsShared(0);
      reportAdminMapper.updatePlanVisible(planReportDTO);
      return 200;
    } catch (Exception e) {
      return 500;
    }
  }
}

