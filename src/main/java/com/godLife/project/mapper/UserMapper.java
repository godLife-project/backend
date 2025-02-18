package com.godLife.project.mapper;

import com.godLife.project.dto.datas.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT USER_IDX, USER_ID, USER_PW FROM USER_TABLE WHERE USER_ID = #{userId}")
    UserDTO UserLogin(@Param("userId") String userId);
}
