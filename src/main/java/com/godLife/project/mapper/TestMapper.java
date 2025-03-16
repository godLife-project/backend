package com.godLife.project.mapper;

import com.godLife.project.dto.test.GetPlanIdxDTO;
import com.godLife.project.dto.test.GetUserListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TestMapper {
    @Select("SELECT PLAN_IDX, USER_IDX, IS_DELETED FROM PLAN_TABLE")
    List<GetPlanIdxDTO> findPlanIdx();
    @Select("SELECT USER_IDX, USER_NAME, USER_ID, USER_JOIN, AUTHORITY_IDX, USER_NICK, NICK_TAG FROM USER_TABLE")
    List<GetUserListDTO> getUserList();
    @Update("UPDATE PLAN_TABLE SET REVIEW = NULL WHERE PLAN_IDX = #{planIdx}")
    void deleteReview(int planIdx);
    @Update("UPDATE PLAN_TABLE SET IS_ACTIVE = #{isActive}, IS_COMPLETED = #{isCompleted}, IS_DELETED = #{isDeleted}, IS_SHARED = #{isShared} WHERE PLAN_IDX = #{planIdx}")
    void changePlanStatus(int isActive, int isCompleted, int isDeleted, int isShared, int planIdx);
}
