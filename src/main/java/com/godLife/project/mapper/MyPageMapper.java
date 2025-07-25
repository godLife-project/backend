package com.godLife.project.mapper;

import com.godLife.project.dto.datas.UserDTO;
import com.godLife.project.dto.request.myPage.*;
import com.godLife.project.dto.response.MyPageUserInfosResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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
    MyPageUserInfosResponseDTO getUserInfos(int userIdx);

    // 회원 정보 수정
    int modifyPersonal(ModifyPersonalRequestDTO modifyPersonalRequestDTO);
    // 닉네임 수정
    int modifyNickName(ModifyNicknameRequestDTO modifyNicknameRequestDTO);
    // 이메일 수정
    @Update("UPDATE USER_TABLE SET USER_EMAIL = #{userEmail} WHERE USER_IDX = #{userIdx} AND IS_DELETED = 'N'")
    int modifyEmail(ModifyEmailRequestDTO modifyEmailRequestDTO);
    // 직업/목표 수정
    @Update("UPDATE USER_TABLE SET JOB_IDX = #{jobIdx}, TARGET_IDX = #{targetIdx} WHERE USER_IDX = #{userIdx} AND IS_DELETED = 'N'")
    int modifyJobTarget(ModifyJobTargetRequestDTO modifyJobTargetRequestDTO);

    // 비밀번호 수정
    @Update("UPDATE USER_TABLE SET USER_PW = #{userPw} WHERE USER_IDX = #{userIdx} AND IS_DELETED = 'N'")
    int modifyPassword(GetUserPwRequestDTO userPwRequestDTO);

    // 선택 루틴 일괄 삭제
    int deleteSelectPlans(int userIdx, List<Integer> planIndexes);
    // 선택 루틴 일괄 비/공개 전환
    int switchIsSharedBySelectPlans(int userIdx, List<Integer> planIndexes, String mode);

    // 선택 루틴 일괄 좋아요 취소
    int unLikeSelectPlans(int userIdx, List<Integer> planIndexes);
}
