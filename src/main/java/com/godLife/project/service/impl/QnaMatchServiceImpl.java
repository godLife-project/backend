package com.godLife.project.service.impl;

import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;
import com.godLife.project.dto.serviceAdmin.AdminIdxAndIdDTO;
import com.godLife.project.mapper.autoMatch.AutoMatchMapper;
import com.godLife.project.service.interfaces.QnaMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class QnaMatchServiceImpl implements QnaMatchService {

    private final AutoMatchMapper autoMatchMapper;
    private final RedissonClient redissonClient;

    private static final String QNA_LOCK = "qna-lock:";


    @Override
    public boolean matchSingleQna(int qnaIdx, int adminIdx) {
        RLock lock = redissonClient.getLock(QNA_LOCK + qnaIdx);

        try {
            if (lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                Integer matchedAdminIdx = autoMatchMapper.getAdminIdxForQna(qnaIdx);
                if (matchedAdminIdx == null) {
                    // 할당된 관리자가 없을 경우 할당 진행
                    return autoMatchMapper.autoMatchSingleQna(qnaIdx, adminIdx);
                } else {
                    return false;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return false;
    }

    @Override
    public AdminIdxAndIdDTO getAdminInfo() {
        return autoMatchMapper.getServiceAdminIdx();
    }

    @Override
    public WaitListMessageDTO setWaitListForMessage(int qnaIdx, String status) {
        QnaWaitListDTO waitItem = new QnaWaitListDTO();
        waitItem.setQnaIdx(qnaIdx);

        WaitListMessageDTO waitQna = new WaitListMessageDTO();
        waitQna.setWaitQnA(Collections.singletonList(waitItem));
        waitQna.setStatus(status);

        return waitQna;
    }
}
