package com.godLife.project.service.impl.jwtImpl;

import com.godLife.project.dto.jwtDTO.RefreshDTO;
import com.godLife.project.mapper.VerifyMapper;
import com.godLife.project.mapper.jwtMapper.RefreshMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.jwtInterface.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

  private final RefreshMapper refreshMapper;
  private final VerifyMapper verifyMapper;

  private final RedisService redisService;
  private static final String SAVE_SERVICE_ADMIN_STATUS = "save-service-admin-status:";
  private static final String IS_LOGOUT_ADMIN = "admin-is-logout:";

  // 리프레쉬 토큰 유무 확인
  @Override
  public Boolean existsByRefresh(String refresh) {
    return refreshMapper.existsByRefresh(refresh) > 0;
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
    refreshDTO.setExpiration(date);

    refreshMapper.addRefreshToken(refreshDTO);
  }

  @Override
  public void deleteAdminStatusByRedis(String userId) {
    int userIdx = verifyMapper.getUserIdxByUserId(userId);

    System.out.println("deleteAdminStatusByRedis 동작.. userIdx : " + userId);
    String isSaved = redisService.getStringData(SAVE_SERVICE_ADMIN_STATUS + userIdx);
    if (isSaved != null) {
      System.out.println("데이터 삭제");
      redisService.deleteData(SAVE_SERVICE_ADMIN_STATUS + userIdx);
    }
    redisService.saveStringData(IS_LOGOUT_ADMIN + userIdx, "true", 's', 10);
  }
}
