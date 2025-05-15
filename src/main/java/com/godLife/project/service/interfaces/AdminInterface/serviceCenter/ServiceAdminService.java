package com.godLife.project.service.interfaces.AdminInterface.serviceCenter;

import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminInfos;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminList;

import java.util.List;

public interface ServiceAdminService {

  /**
   * 권한 3,4,6,7 을 갖고 있는 관리자가 로그인 시,
   * DB의 SERVICE_CENTER 테이블에 관리자 정보를 저장합니다.
   * @param username 로그인 한 유저의 아이디
   */
  void setCenterLoginByAdmin3467(String username);

  /**
   * 로그아웃 시 재발급 토큰을 통해 유저의 인덱스 번호를 조회 후
   * SERVICE_CENTER 테이블에 해당 인덱스 번호를 갖고 있는 행을 삭제합니다.
   * @param refreshToken 로그아웃 하려는 유저의 재발급 토큰
   */
  @Deprecated
  void setCenterLogoutByAdmin3467(String refreshToken);

  /**
   * 관리자의 상태를 전환 합니다.
   * 상태 : 자동 매칭을 받을 지 말 지를 결정함.
   * @param userIdx 상태를 전환 할 유저의 인덱스 번호
   * @return String - 활성화 / 비활성화
   */
  String switchAdminStatus(int userIdx);

  /**
   * 관리자 상태 활성화로 전환
   * @param userIdx 상태를 전환 할 유저의 인덱스 번호
   */
  void setAdminStatusTrue(int userIdx);

  /**
   * <strong>관리자 서비스 센터 연결해제 처리</strong>
   * @param username 연결 해제 할 관리자의 아이디
   */
  void disconnectAdminServiceCenter(String username);

  /**
   * 현재 로그인 한 유저의 상태를 반환합니다.
   * 보통 관리자 페이지 최초 진입 시 상태 정보를 전달하기 위해 만들었습니다.
   * @param userIdx 상태를 조회할 유저의 인덱스 번호
   * @return String - 활성화 / 비활성화
   */
  String getAdminStatus(int userIdx);

  /**
   * 해당 관리자의 현재 응대중 인 문의의 개수를 업데이트 합니다.
   * @param adminIdx 업데이트 할 관리자의 인덱스 번호
   */
  void refreshMatchCount(int adminIdx);

  /**
   * <strong>현재 1:1 문의 서비스 관리자 페이지에 접속하고 있는 관리자 정보를 조회 합니다.</strong>
   * <p>{@code userName} - String - 관리자 이름</p>
   * <p>{@code status} - int - 자동매칭 활성화 여부</p>
   * <p>{@code matched} - int - 매칭된(응대중인) 문의 수</p>
   * @return {@code List<ServiceCenterAdminList>}
   */
  List<ServiceCenterAdminInfos> getAllAccessServiceAdminList();

  /**
   * <strong>STOMP 응답 메시지 패키징 메서드 ::</strong>
   * 상담원 명단 최신화를 위해 DTO를 패키징 합니다.
   * @param accessAdminInfos {@code List<ServiceCenterAdminInfos>} 상담원 리스트를 조회한 데이터를 넣어주세요.
   * @return List<ServiceCenterAdminList>
   */
  List<ServiceCenterAdminList> getAccessAdminListForMessage(List<ServiceCenterAdminInfos> accessAdminInfos);
}
