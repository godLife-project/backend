package com.godLife.project.mapper.jwtMapper;

import com.godLife.project.dto.jwtDTO.RefreshDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RefreshMapper {

  @Select("SELECT COUNT(*) FROM REFRESH_TOKEN WHERE REFRESH = #{refresh}")
  Boolean existsByRefresh(String refresh);

  @Delete("DELETE FROM REFRESH_TOKEN WHERE REFRESH = #{refresh}")
  void deleteByRefresh(String refresh);

  @Insert("INSERT INTO REFRESH_TOKEN VALUES (REFRESH_SEQ.NEXTVAL, #{username}, #{refresh}, #{expiration})")
  void addRefreshToken(RefreshDTO refreshDTO);
}
