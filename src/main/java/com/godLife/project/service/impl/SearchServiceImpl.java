package com.godLife.project.service.impl;


import com.godLife.project.dto.infos.SearchLogDTO;
import com.godLife.project.dto.response.SearchLogsResponseDTO;
import com.godLife.project.mapper.SearchMapper;
import com.godLife.project.service.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchMapper searchMapper;

    @Override
    public void setSearchLog(SearchLogDTO searchLogDTO) {
        List<SearchLogDTO> keywords = searchMapper.getRecentKeyword(searchLogDTO);
        for (SearchLogDTO keyword : keywords) {
            if (Objects.equals(keyword.getSearchKeyword(), searchLogDTO.getSearchKeyword())) {
                log.info("이전 검색어 중복.. 업데이트 처리");
                searchMapper.updateSearchLog(keyword);
                return;
            }
        }
        searchMapper.setSearchLogs(searchLogDTO);
    }

    @Override
    public List<SearchLogsResponseDTO> getSearchLogs(SearchLogDTO searchLogDTO) {
        return searchMapper.getSearchLogs(searchLogDTO);
    }

    @Override
    public int deleteSearchLog(SearchLogDTO searchLogDTO) {
        try {
            int result = searchMapper.deleteSearchLog(searchLogDTO);

            if (result == 0) {
                return 412; // 조건 충족 안됨
            }
            return 204;
        } catch (Exception e) {
            log.error("e: ", e);
            return 500;
        }

    }

}
