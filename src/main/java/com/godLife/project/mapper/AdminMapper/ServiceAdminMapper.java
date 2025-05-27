package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminInfos;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminList;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ServiceAdminMapper {

  // 로그인 시 서비스 센터 접근 가능 유저 로그인 처리
  @Insert("INSERT INTO SERVICE_CENTER(USER_IDX) VALUES(#{userIdx})")
  void setCenterLoginByAdmin3467(int userIdx);

  // 로그아웃 시 서비스 센터 로그인 유저 로그아웃 처리
  int setCenterLogoutByAdmin3467(String refreshToken);

  // 매칭된 혹은 응대중 인 문의 수 업데이트
  void setMatchedByQuestionCount(int userIdx, List<String>notStatus);

  // 관리자 상태 비/활성화 하기
  int switchAdminStatus(int userIdx);
  // 관리자 상태 활성화 하기
  @Update("UPDATE SERVICE_CENTER SET STATUS = 1 WHERE USER_IDX = #{userIdx}")
  void setAdminStatusTrue(int userIdx);

  // 서비스 센터 연결 해제 처리
  @Delete("DELETE FROM SERVICE_CENTER WHERE USER_IDX = #{userIdx}")
  void disconnectAdminServiceCenter(int userIdx);

  // 관리자 상태 조회
  @Select("SELECT STATUS FROM SERVICE_CENTER WHERE USER_IDX = #{userIdx}")
  boolean getServiceAdminStatus(int userIdx);

  // 접속중인 상담원 목록 조회
  @Select("""
      SELECT S.USER_IDX, U.USER_NAME, S.STATUS, S.MATCHED
        FROM SERVICE_CENTER S
      INNER JOIN USER_TABLE U ON S.USER_IDX = U.USER_IDX
      ORDER BY S.USER_IDX""")
  List<ServiceCenterAdminInfos> getAllAccessServiceAdminList();

}
