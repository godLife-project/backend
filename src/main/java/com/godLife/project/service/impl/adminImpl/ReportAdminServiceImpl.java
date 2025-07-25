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


// 유저 신고 조회 (전체 or 상태별)
  public List<UserReportDTO> getAllReports(Integer status, int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.getAllReports(status, size, offset);
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
    Integer isApproved = userReportDTO.getIsApproved();
    Integer reportIdx = userReportDTO.getUserReportIdx();

    if (isApproved == null || (isApproved != 0 && isApproved != 1)) {
      throw new IllegalArgumentException("isApproved 값은 0(거절) 또는 1(승인)이어야 합니다.");
    }

    // 신고 상태를 '처리 완료'로 설정
    userReportDTO.setStatus(1);
    reportAdminMapper.userReportStateUpdate(userReportDTO);

    // 승인인 경우에만 후속 처리
    if (isApproved == 1) {
      Integer reportedIdx = reportAdminMapper.findReportedIdxByReportIdx(reportIdx);
      if (reportedIdx == null) {
        throw new IllegalArgumentException("REPORTED_IDX를 찾을 수 없습니다.");
      }
      // 신고 횟수 1 증가
      reportAdminMapper.incrementReportCount(reportedIdx);

      // 현재 신고 횟수 조회
      Integer reportCount = reportAdminMapper.getReportCount(reportedIdx);
      if (reportCount != null && reportCount >= 5) {
        reportAdminMapper.banUser(reportedIdx); // 유저 정지 처리
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

