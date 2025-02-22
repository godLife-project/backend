package com.godLife.project.service.jwt;

import com.godLife.project.dto.jwt.RefreshDTO;

public interface RefreshService {

  // 리프레쉬 토큰 유무 확인
  Boolean existsByRefresh(String refresh);

  // 리프레쉬 토큰 삭제
  void deleteByRefresh(String refresh);

  // 리프레쉬 토큰 등록
  void addRefreshToken(String username, String refresh, Long expiredMs);
}
