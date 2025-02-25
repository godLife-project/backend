package com.godLife.project.controller.jwtController;


import com.godLife.project.service.impl.jwtImpl.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {

  private final ReissueService reissueService;

  public ReissueController(ReissueService reissueService) {
    this.reissueService = reissueService;
  }

  @PostMapping("api/reissue")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    return reissueService.reissueToken(request, response);
  }
}
