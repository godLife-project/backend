package com.godLife.project.mapper;

import com.godLife.project.dto.test.GetPlanIdxDTO;
import com.godLife.project.dto.test.GetUserListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper {
    @Select("SELECT PLAN_IDX, USER_IDX, IS_DELETED FROM PLAN_TABLE")
    List<GetPlanIdxDTO> findPlanIdx();
    @Select("SELECT USER_IDX, USER_NAME, USER_ID, USER_JOIN, AUTHORITY_IDX, USER_NICK, NICK_TAG FROM USER_TABLE")
    List<GetUserListDTO> getUserList();
}
