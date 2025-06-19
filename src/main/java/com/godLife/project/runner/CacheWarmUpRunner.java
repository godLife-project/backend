package com.godLife.project.runner;

import com.godLife.project.mapper.CategoryMapper;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheWarmUpRunner implements ApplicationRunner {

  private final RedisService redisService;
  private final CategoryMapper categoryMapper;
  private final CategoryService categoryService;
  private final QnaMapper qnaMapper;

  @Override
  public void run(ApplicationArguments args) {
    log.info("ApplicationRunner - 캐시 워밍업 시작..");
    log.info("탑 메뉴 카테고리 워밍업 준비..");
    redisService.saveListData("category::topMenu", categoryService.getProcessedAllTopCategories(), 'n', 0);
    log.info("직업 카테고리 워밍업 준비..");
    redisService.saveListData("category::job", categoryMapper.getAllJOBCategories(), 'n', 0);
    log.info("목표 카테고리 워밍업 준비..");
    redisService.saveListData("category::target", categoryMapper.getAllTargetCategories(), 'n', 0);
    log.info("챌린지 카테고리 워밍업 준비..");
    redisService.saveListData("category::chall", categoryMapper.getAllChallCategories(), 'n', 0);
    log.info("아이콘 정보 워밍업 준비..");
    redisService.saveListData("category::userIcon", categoryMapper.getUserIconInfos(), 'n', 0);
    log.info("관리자 아이콘 정보 워밍업 준비..");
    redisService.saveListData("category::adminIcon", categoryMapper.getAllIconInfos(), 'n', 0);
    log.info("불꽃 정보 워밍업 준비..");
    redisService.saveListData("category::fire", categoryMapper.getAllFireInfos(), 'n', 0);
    log.info("유저 레벨 정보 워밍업 준비..");
    redisService.saveListData("category::userLv", categoryMapper.getAllUserLevelInfos(), 'n', 0);
    log.info("대기중 문의 큐에 업로드 준비..");
    List<Integer> qnaIndexes = qnaMapper.getlistWaitQnaIdx();
    if (qnaIndexes != null && !qnaIndexes.isEmpty()) {
      for (int qnaIdx : qnaIndexes) {
        redisService.leftPushToRedisQueue("qna_queue", String.valueOf(qnaIdx));
      }
    }
    log.info("워밍업 완료!!!");
  }
}
