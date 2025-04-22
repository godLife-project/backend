package com.godLife.project.mapper.autoMatch;

import com.godLife.project.dto.serviceAdmin.AdminIdxAndIdDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AutoMatchMapper {

  // 매칭 할 상담원 조회
  AdminIdxAndIdDTO getServiceAdminIdx();

  // 문의 등록 시 단일 문의 자동 매칭
  boolean autoMatchSingleQna(int qnaIdx, Integer adminIdx);

  // 할당할 문의의 할당된 관리자 유무 확인
  @Select("SELECT A_USER_IDX FROM QNA_TABLE WHERE QNA_IDX = #{qnaIdx}")
  Integer getAdminIdxForQna(int qnaIdx);
}
