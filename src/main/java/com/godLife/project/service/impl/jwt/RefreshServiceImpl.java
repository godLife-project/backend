package com.godLife.project.service.impl.jwt;

import com.godLife.project.dto.jwt.RefreshDTO;
import com.godLife.project.mapper.jwt.RefreshMapper;
import com.godLife.project.service.jwt.RefreshService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class RefreshServiceImpl implements RefreshService {

  private final RefreshMapper refreshMapper;

  public RefreshServiceImpl(RefreshMapper refreshMapper) {
    this.refreshMapper = refreshMapper;
  }

  // 리프레쉬 토큰 유무 확인
  @Override
  public Boolean existsByRefresh(String refresh) {
    return refreshMapper.existsByRefresh(refresh);
  }

  // 리프레쉬 토큰 삭제
  @Override
  public void deleteByRefresh(String refresh) {
    refreshMapper.deleteByRefresh(refresh);
  }

  // 리프레쉬 토큰 등록
  @Override
  public void addRefreshToken(String username, String refresh, Long expiredMs) {

    Date date = new Date(System.currentTimeMillis() + expiredMs);

    RefreshDTO refreshDTO = new RefreshDTO();
    refreshDTO.setUsername(username);
    refreshDTO.setRefresh(refresh);
    refreshDTO.setExpiration(date.toString());

    refreshMapper.addRefreshToken(refreshDTO);
  }
}
