package com.godLife.project.dto.datas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IconDTO {
  @Schema(description = "아이콘 매핑", example = "video")
  private String iconKey;

  @Schema(description = "아이콘 코드", example = "Video")
  private String icon;

  @Schema(description = "색상 코드", example = "#800080")
  private String color;

  @Schema(description = "사용 여부", example = "0")
  private int visible;
}
