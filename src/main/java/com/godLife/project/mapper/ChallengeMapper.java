package com.godLife.project.mapper;

import com.godLife.project.dto.contents.ChallengeDTO;

import java.util.List;

public interface ChallengeMapper {
    List<ChallengeDTO> getLatestChallenges();
}
