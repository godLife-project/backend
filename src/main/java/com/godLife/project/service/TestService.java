package com.godLife.project.service;

import com.godLife.project.dto.TestDTO;
import com.godLife.project.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public List<String> selectAll() {
        return testMapper.selectAll();
    }

}
