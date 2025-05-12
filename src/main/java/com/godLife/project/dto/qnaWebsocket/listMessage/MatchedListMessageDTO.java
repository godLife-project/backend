package com.godLife.project.dto.qnaWebsocket.listMessage;

import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import lombok.Data;

import java.util.List;

@Data
public class MatchedListMessageDTO {

  private List<QnaMatchedListDTO> matchedQnA;

  private String status;
  // private String roomNo;

}
