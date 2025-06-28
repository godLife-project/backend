package com.godLife.project.dto.serviceAdmin;

import lombok.Data;


@Data
public class ServiceCenterAdminList {
  // 관리자 이름
  private String userName;
  // 관리자 이메일
  private String userEmail;
  // 자동 매칭 활성화 여부
  private int status;
  // 매칭된(응대중인) 문의 수
  private int matched;
}
