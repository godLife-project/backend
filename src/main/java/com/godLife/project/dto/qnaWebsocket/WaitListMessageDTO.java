package com.godLife.project.dto.qnaWebsocket;

import lombok.Data;

import java.util.List;

@Data
public class WaitListMessageDTO {

  private List<QnaWaitListDTO> waitQnaInfos;

  private String roomNo;

}
