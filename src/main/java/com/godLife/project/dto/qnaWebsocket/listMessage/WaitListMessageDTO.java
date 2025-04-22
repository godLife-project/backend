package com.godLife.project.dto.qnaWebsocket.listMessage;

import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import lombok.Data;

import java.util.List;

@Data
public class WaitListMessageDTO {

  private List<QnaWaitListDTO> waitQnA;

  private String status;
  // private String roomNo;

}
