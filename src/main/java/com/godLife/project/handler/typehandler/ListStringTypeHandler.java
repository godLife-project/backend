package com.godLife.project.handler.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ListStringTypeHandler extends BaseTypeHandler<List<String>> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
    // List<String>을 "mon,tue,wed"와 같은 형태로 저장
    String result = String.join(",", parameter);
    ps.setString(i, result);
  }

  @Override
  public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    // DB에서 데이터를 가져와서 List<String>으로 변환
    String result = rs.getString(columnName);
    if (result != null) {
      return Arrays.asList(result.split(","));
    }
    return null;
  }

  @Override
  public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    // DB에서 데이터를 가져와서 List<String>으로 변환
    String result = rs.getString(columnIndex);
    if (result != null) {
      return Arrays.asList(result.split(","));
    }
    return null;
  }

  @Override
  public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    // DB에서 데이터를 가져와서 List<String>으로 변환
    String result = cs.getString(columnIndex);
    if (result != null) {
      return Arrays.asList(result.split(","));
    }
    return null;
  }
}

