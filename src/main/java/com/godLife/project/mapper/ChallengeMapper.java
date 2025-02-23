package com.godLife.project.mapper;

import com.godLife.project.dto.contents.ChallengeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChallengeMapper {
    List<ChallengeDTO> getLatestChallenges();
}
