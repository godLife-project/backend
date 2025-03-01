package com.godLife.project.service;


import com.godLife.project.dto.test.GetPlanIdxDTO;
import com.godLife.project.dto.test.GetUserListDTO;

import java.util.List;

public interface TestService {
    List<GetPlanIdxDTO> findPlanIdx();
    List<GetUserListDTO> getUserList();
}
