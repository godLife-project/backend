package com.godLife.project.mapper;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.myPage.ModifyEmailRequestDTO;
import com.godLife.project.dto.request.myPage.ModifyNicknameRequestDTO;
import com.godLife.project.dto.request.myPage.ModifyPersonalRequestDTO;
import com.godLife.project.dto.response.MyPageUserInfosResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MyPageMapper {
    // 회원 탈퇴
    int deleteAccount(int userIdx);
    // 탈퇴 취소
    int deleteCancelAccount(int userIdx);

    // 암호화 비밀번호 조회
    @Select("SELECT USER_PW FROM USER_TABLE WHERE USER_IDX = #{userIdx} AND NOT IS_DELETED = 'D'")
    String getEncryptPassword(int userIdx);

    // 회원 정보 조회
    @Select("SELECT USER_EMAIL, USER_PHONE, USER_GENDER, USER_JOIN, USER_ID\n" +
            "  FROM USER_TABLE\n" +
            " WHERE USER_IDX = #{userIdx} AND IS_DELETED = 'N'")
    MyPageUserInfosResponseDTO getUserInfos(int userIdx);

    // 회원 정보 수정
    int modifyPersonal(ModifyPersonalRequestDTO modifyPersonalRequestDTO);
    // 닉네임 수정
    int modifyNickName(ModifyNicknameRequestDTO modifyNicknameRequestDTO);
    // 이메일 수정
    @Update("UPDATE USER_TABLE SET USER_EMAIL = #{userEmail} WHERE USER_IDX = #{userIdx} AND IS_DELETED = 'N'")
    int modifyEmail(ModifyEmailRequestDTO modifyEmailRequestDTO);
}
