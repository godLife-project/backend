package com.godLife.project.dto.qnaWebsocket.listMessage;

import com.godLife.project.dto.qnaWebsocket.QnaReplyListDTO;
import lombok.Data;

import java.util.List;

@Data
public class QnaDetailMessageDTO {

  private int qnaIdx;

  private String body;

  private String status;

  private List<QnaReplyListDTO> comments;
}
