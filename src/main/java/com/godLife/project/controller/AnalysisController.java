package com.godLife.project.controller;

import com.godLife.project.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {
  private final GlobalExceptionHandler handler;
}
