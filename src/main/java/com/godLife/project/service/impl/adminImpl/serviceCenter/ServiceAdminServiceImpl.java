package com.godLife.project.service.impl.adminImpl.serviceCenter;

import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.AdminMapper.ServiceAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceAdminServiceImpl implements ServiceAdminService {

  private final ServiceAdminMapper serviceAdminMapper;

  // 고객서비스 접근 가능 관리자 로그인 처리
  @Override
  public void setCenterLoginByAdmin3467(int userIdx) {
    try  {
      serviceAdminMapper.setCenterLoginByAdmin3467(userIdx);
      log.info("AdminService - setCenterLoginByAdmin3467 :: 저장 성공..! 응대중 인 문의의 개수를 업데이트 합니다.");
      serviceAdminMapper.setMatchedByQuestionCount(userIdx);
      log.info("AdminService - setCenterLoginByAdmin3467 :: 응대중 인 문의의 개수를 업데이트 완료.");
    } catch (DataIntegrityViolationException e) {
      log.warn("AdminService - setCenterLoginByAdmin3467 :: 이미 로그인 처리된 관리자 입니다. 로그인 등록을 건너 뜁니다.");
      serviceAdminMapper.setMatchedByQuestionCount(userIdx);
      log.info("AdminService - setCenterLoginByAdmin3467 :: 등록을 건너 뛴 후 응대중 인 문의의 개수 업데이트 완료.");
    } catch (Exception e) {
      log.error("AdminService - setCenterLoginByAdmin3467 :: 고객센터 로그인 처리 중 문제가 발생했습니다: ", e);
    }
  }

  // 고객관리자 로그아웃 처리
  @Override
  public void setCenterLogoutByAdmin3467(String refreshToken) {
    try {
      int result = serviceAdminMapper.setCenterLogoutByAdmin3467(refreshToken);
      if (result == 0) {
        log.info("AdminService - setCenterLogoutByAdmin3467 :: 관리자가 아니거나, 이미 로그아웃 처리된 상태입니다.");
        return;
      }
      log.info("AdminService - setCenterLogoutByAdmin3467 :: 삭제 성공");
    } catch (Exception e) {
      log.error("AdminService - setCenterLogoutByAdmin3467 :: 알 수 없는 오류가 발생했습니다.", e);
    }
  }

  // 관리자 상태 비/활성화 하기
  @Override
  public String switchAdminStatus(int userIdx) {
    try {
      int result = serviceAdminMapper.switchAdminStatus(userIdx);

      if (result == 0) {
        log.warn("AdminService - switchAdminStatus :: 관리자가 아니거나, 로그아웃 처리된 상태입니다.");
        throw new CustomException("관리자가 아니거나, 로그아웃 처리된 상태입니다.", HttpStatus.NOT_FOUND);
      }

      return serviceAdminMapper.getServiceAdminStatus(userIdx) ? "활성화" : "비활성화";
    } catch (CustomException e) {
      throw e;

    }catch (Exception e) {
      log.error("AdminService - switchAdminStatus :: 알 수 없는 오류가 발생했습니다.", e);
      throw new CustomException("DB 오류가 발생했습니다. 관리자에게 문의 해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public String getAdminStatus(int userIdx) {
    try {
      return serviceAdminMapper.getServiceAdminStatus(userIdx) ? "활성화" : "비활성화";
    } catch (PersistenceException e) {
      log.error("AdminService - getAdminStatus :: 관리자가 없거나 파라미터 혹은 결과가 null 입니다.", e);
      throw new CustomException("관리자가 없거나, 파라미터 혹은 결과가 null 입니다.", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      log.error("AdminService - getAdminStatus :: 알 수 없는 오류가 발생했습니다.", e);
      throw new CustomException("DB 오류가 발생했습니다. 관리자에게 문의 해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void refreshMatchCount(int adminIdx) {
    serviceAdminMapper.setMatchedByQuestionCount(adminIdx);
  }
}
