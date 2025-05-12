package com.godLife.project.mapper;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.GetNameNEmail;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // 회원가입
    @Insert("INSERT INTO USER_TABLE(USER_IDX, USER_NAME, USER_ID, USER_PW, USER_NICK, NICK_TAG, USER_EMAIL, JOB_IDX, TARGET_IDX, USER_PHONE, USER_GENDER)" +
            "VALUES (USER_SEQ.NEXTVAL, #{userName}, #{userId}, #{userPw}, #{userNick}, #{nickTag}, #{userEmail}, #{jobIdx}, #{targetIdx}, #{userPhone}, #{userGender})")
    void insertUser(UserDTO joinUserDTO);

    // 아이디 중복 체크
    @Select("SELECT COUNT(*) FROM USER_TABLE WHERE USER_ID = #{userId} AND IS_DELETED = 'N'")
    Boolean checkUserIdExist(String userId);

    // 이메일 중복 체크
    @Select("SELECT COUNT(*) FROM USER_TABLE WHERE USER_EMAIL = #{userEmail} AND IS_DELETED = 'N'")
    Boolean checkUserEmailExist(String userEmail);

    // 닉네임 중복 체크
    @Select("SELECT COUNT(*) FROM USER_TABLE WHERE USER_NICK = #{userNick} AND IS_DELETED = 'N'")
    int checkUserNickExist(String userNick);

    // 유저인덱스로 유저 아이디 조회
    @Select("SELECT USER_ID FROM USER_TABLE WHERE USER_IDX = #{userIdx}")
    String getUserIdByUserIdx(int userIdx);

    // 로그인
    UserDTO findByUserid(String username);

    // 관리자 권환 확인
    @Select("SELECT AUTHORITY_IDX FROM USER_TABLE WHERE USER_ID = #{userId}")
    String getUserAuthority(@Param("userId") String userId);

    // 아이디 찾기
    @Select("SELECT USER_ID FROM USER_TABLE WHERE USER_NAME = #{userName} AND USER_EMAIL = #{userEmail} AND IS_DELETED = 'N'")
    String getUserId(GetNameNEmail getNameNEmail);

    // 비밀번호 찾기 (변경)
    @Update("UPDATE USER_TABLE SET USER_PW = #{userPw} WHERE USER_EMAIL = #{userEmail} AND IS_DELETED = 'N'")
    int findUserPw(String userPw, String userEmail);

}
