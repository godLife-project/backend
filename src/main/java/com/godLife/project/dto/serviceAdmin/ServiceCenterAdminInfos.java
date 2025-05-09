package com.godLife.project.dto.serviceAdmin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceCenterAdminInfos extends ServiceCenterAdminList{
  private int userIdx;

  public ServiceCenterAdminInfos() {
    super();
  }

  public ServiceCenterAdminInfos(ServiceCenterAdminList base) {
    super.setUserName(base.getUserName());
    super.setMatched(base.getMatched());
    super.setStatus(base.getStatus());
  }
}
