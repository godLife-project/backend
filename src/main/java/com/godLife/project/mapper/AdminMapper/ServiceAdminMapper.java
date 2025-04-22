package com.godLife.project.mapper.AdminMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ServiceAdminMapper {

  // 로그인 시 서비스 센터 접근 가능 유저 로그인 처리
  @Update("INSERT INTO SERVICE_CENTER(USER_IDX) VALUES(#{userIdx})")
  void setCenterLoginByAdmin3467(int userIdx);
  // 로그아웃 시 서비스 센터 로그인 유저 로그아웃 처리
  int setCenterLogoutByAdmin3467(String refreshToken);

  // 매칭된 혹은 응대중 인 문의 수 업데이트
  void setMatchedByQuestionCount(int userIdx);

  // 관리자 상태 비/활성화 하기
  int switchAdminStatus(int userIdx);
  // 관리자 상태 조회
  @Select("SELECT STATUS FROM SERVICE_CENTER WHERE USER_IDX = #{userIdx}")
  boolean getServiceAdminStatus(int userIdx);

}
