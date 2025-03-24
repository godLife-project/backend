package com.godLife.project.mapper;

import com.godLife.project.dto.infos.SearchLogDTO;
import com.godLife.project.dto.response.SearchLogsResponseDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SearchMapper {

    // 검색 기록 추가
    void setSearchLogs(SearchLogDTO searchLogDTO);
    // 검색 기록 조회
    List<SearchLogsResponseDTO> getSearchLogs(SearchLogDTO searchLogDTO);

    // 최근 검색어 조회
    List<SearchLogDTO> getRecentKeyword(SearchLogDTO searchLogDTO);
    // 검색 기록 최신화
    void updateSearchLog(SearchLogDTO searchLogDTO);

    // 검색 기록 삭제
    int deleteSearchLog(SearchLogDTO searchLogDTO);
}
