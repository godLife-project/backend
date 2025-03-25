package com.godLife.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MyPageMapper {
    // 회원 탈퇴
    int deleteAccount(int userIdx);

    // 탈퇴 취소
    int deleteCancelAccount(int userIdx);

    // 암호화 비밀번호 조회
    @Select("SELECT USER_PW FROM USER_TABLE WHERE USER_IDX = #{userIdx} AND NOT IS_DELETED = 'D'")
    String getEncryptPassword(int userIdx);
}
