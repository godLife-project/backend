package com.godLife.project.service;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.datas.UserDTO;

import java.util.List;

public interface TestService {
    UserDTO getUserById(int userIdx);
    List<String> getJobName();
    List<UserDTO> getAllUsers();

    List<JobCateDTO> getJobAll();
}
