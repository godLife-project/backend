package com.godLife.project.service.impl;

import com.godLife.project.dto.test.GetPlanIdxDTO;
import com.godLife.project.dto.test.GetUserListDTO;
import com.godLife.project.mapper.TestMapper;
import com.godLife.project.service.interfaces.TestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private final TestMapper testMapper;

    // 생성자 주입
    public TestServiceImpl(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    @Override
    public List<GetPlanIdxDTO> findPlanIdx() { return testMapper.findPlanIdx(); }
    @Override
    public List<GetUserListDTO> getUserList() { return testMapper.getUserList(); }

    @Override
    public void deleteReview(int planIdx) { testMapper.deleteReview(planIdx); }

    @Override
    public void changePlanStatus(int isActive, int isCompleted, int isDeleted, int isShared, int planIdx) {
        testMapper.changePlanStatus(isActive, isCompleted, isDeleted, isShared, planIdx);
    }
}
