package com.godLife.project.service.interfaces.adminInterface;

public interface ServiceAdminService {

  // 고객서비스 접근 가능 관리자 로그인 처리
  void setCenterLoginByAdmin3467(int userIdx);
  // 고객관리자 로그아웃 처리
  void setCenterLogoutByAdmin3467(String refreshToken);

  // 관리자 상태 비/활성화 하기
  String switchAdminStatus(int userIdx);
}
