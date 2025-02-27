package com.godLife.project.mapper;

import com.godLife.project.dto.datas.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    // 회원가입
    @Insert("INSERT INTO USER_TABLE(USER_IDX, USER_NAME, USER_ID, USER_PW, USER_NICK, NICK_TAG, USER_EMAIL, JOB_IDX, TARGET_IDX, USER_PHONE, USER_GENDER)" +
            "VALUES (USER_SEQ.NEXTVAL, #{userName}, #{userId}, #{userPw}, #{userNick}, #{nickTag}, #{userEmail}, #{jobIdx}, #{targetIdx}, #{userPhone}, #{userGender})")
    void insertUser(UserDTO joinUserDTO);

    // 아이디 중복 체크
    @Select("SELECT COUNT(*) FROM USER_TABLE WHERE USER_ID = #{userId}")
    Boolean checkUserIdExist(String userId);

    // 이메일 중복 체크
    @Select("SELECT COUNT(*) FROM USER_TABLE WHERE USER_EMAIL = #{userEmail}")
    Boolean checkUserEmailExist(String userEmail);

    // 닉네임 중복 체크
    @Select("SELECT COUNT(*) FROM USER_TABLE WHERE USER_NICK = #{userNick}")
    int checkUserNickExist(String userNick);

    // 로그인
    @Select("SELECT * FROM USER_TABLE WHERE USER_ID = #{username}")
    UserDTO findByUserid(String username);

    // 관리자 권환 확인
    @Select("SELECT authority_idx FROM users WHERE user_id = #{userId}")
    Integer getUserAuthority(@Param("userId") Long userId);
}
