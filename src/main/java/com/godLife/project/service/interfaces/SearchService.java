package com.godLife.project.service.interfaces;


import com.godLife.project.dto.infos.SearchLogDTO;
import com.godLife.project.dto.response.SearchLogsResponseDTO;

import java.util.List;

public interface SearchService {
    // 검색 기록 추가
    void setSearchLog(SearchLogDTO searchLogDTO);

    // 검색 기록 조회
    List<SearchLogsResponseDTO> getSearchLogs(SearchLogDTO searchLogDTO);

    // 검색 기록 삭제
    int deleteSearchLog(SearchLogDTO searchLogDTO);
}
