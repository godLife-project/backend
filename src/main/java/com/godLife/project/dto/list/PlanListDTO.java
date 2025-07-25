package com.godLife.project.dto.list;

import com.godLife.project.dto.list.customDTOs.*;
import lombok.Data;

@Data
public class PlanListDTO {
  // 루틴 정보
  private CustomPlanListDTO planInfos;
  // 관심사 정보
  private CustomTargetDTO targetInfos;
  // 직업 정보
  private CustomJobDTO jobDefault;
  // 기타 직업 정보
  private CustomEtcJobDTO jobEtc;
  // 불꽃 정보
  private CustomFireDTO fireInfos;
}
