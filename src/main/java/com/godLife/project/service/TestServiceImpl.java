package com.godLife.project.service;

import com.godLife.project.dto.tables.UserDTO;
import com.godLife.project.mapper.TestMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService{

    private final TestMapper testMapper;

    // 생성자 주입
    public TestServiceImpl(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    @Override
    public UserDTO getUserById(int userIdx) {
        return testMapper.findById(userIdx);
    }

    @Override
    public List<String> getJobName() {
        return testMapper.selectAll();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return testMapper.findAll();
    }
}
