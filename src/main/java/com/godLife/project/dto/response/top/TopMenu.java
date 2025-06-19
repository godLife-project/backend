package com.godLife.project.dto.response.top;

import lombok.Data;

import java.util.List;

@Data
public class TopMenu {
  private int topIdx;
  private String name;
  private String addr;
  private int ordCol;

  private List<TopMenu> children;

}
