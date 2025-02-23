package com.godLife.project.mapper;

import com.godLife.project.dto.datas.UserDTO;
<<<<<<< HEAD
=======
import org.apache.ibatis.annotations.Insert;
>>>>>>> hm
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
<<<<<<< HEAD
    @Select("SELECT USER_IDX, USER_ID, USER_PW FROM USER_TABLE WHERE USER_ID = #{userId}")
    UserDTO UserLogin(@Param("userId") String userId);
=======
    // 회원가입
    @Insert("INSERT INTO USER_TABLE(USER_IDX, USER_NAME, USER_ID, USER_PW, USER_NICK, USER_EMAIL, JOB_IDX, TARGET_IDX, USER_PHONE, USER_GENDER)" +
            "VALUES (USER_SEQ.NEXTVAL, #{userName}, #{userId}, #{userPw}, #{userNick}, #{userEmail}, #{jobIdx}, #{targetIdx}, #{userPhone}, #{userGender})")
    void insertUser(UserDTO joinUserDTO);

    // 아이디 중복 체크
    @Select("SELECT COUNT(*) FROM USER_TABLE WHERE USER_ID = #{userId}")
    Boolean checkUserIdExist(String userId);

    // 로그인
    @Select("SELECT * FROM USER_TABLE WHERE USER_ID = #{username}")
    UserDTO findByUserid(String username);


>>>>>>> hm
}
