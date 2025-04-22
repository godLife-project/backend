package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.contents.QnaReplyDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.MatchedListMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.QnaDetailMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;

public interface QnaService {

  /**
   * 1:1 문의 작성용 메서드 입니다.
   * @param qnaDTO QNA_IDX, Q_USER_IDX, TITLE, CONTENT, CATEGORY 를 필수로 받아야 합니다.
   */
  void createQna(QnaDTO qnaDTO);

  /**
   * 선택한 관리자의 현재 매칭된 문의의 전체 리스트를 조회합니다.
   * STATUS 파라미터를 통해 응답 메세지의 상태를 지정하고,
   * WEBSOCKET 으로 전달할 DTO로 포장해줍니다.
   * @param adminIdx 조회할 관리자의 인덱스 번호
   * @param status 클라이언트에게 보여 줄 상태 값
   * @return MatchedListMessageDTO
   */
  MatchedListMessageDTO getlistAllMatchedQna(int adminIdx, String status);

  /**
   * 특정 문의의 정보를 조회 합니다. 특정 문의가 파라미터로 입력 받은 관리자의 것인지
   * 확인하고, 맞을 경우 STATUS 파라미터를 통해 응답 메세지의 상태를 지정하고,
   * WEBSOCKET 으로 전달할 DTO로 포장해줍니다.
   * @param adminIdx 검증하기 위한 관리자 인덱스 번호
   * @param qnaIdx 조회하기 위한 문의 인덱스 번호
   * @param status 클라이언트에게 보여 줄 상태 값
   * @return MatchedListMessageDTO
   */
  MatchedListMessageDTO getMatchedSingleQna(int adminIdx, int qnaIdx, String status);

  /**
   * 현재 'WAIT' 상태인 문의의 전체 리스트를 조회합니다.
   * STATUS 파라미터를 통해 응답 메세지의 상태를 지정하고,
   * WEBSOCKET 으로 전달할 DTO로 포장해줍니다.
   * @param status 클라이언트에게 보여 줄 상태 값
   * @return WaitListMessageDTO
   */
  WaitListMessageDTO getlistAllWaitQna(String status);

  /**
   * 선택한 문의의 본문만 조회합니다.
   * 조회한 문의는 위험한 html, js 태그를 필터링 한 후 return 합니다.
   * @param qnaIdx 조회할 문의의 인덱스 번호
   * @return String
   */
  String getQnaContent(int qnaIdx);

  /**
   * 문의 답변용 메서드 입니다.
   * 관리자 / 유저 모두가 사용할 수 있고, 서비스 로직 내부적으로
   * 사용자 검증을 합니다. 알아서 관리자와 유저를 걸러냅니다.
   * @param qnaReplyDTO 답변 작성 시 필요한 DTO
   */
  void commentReply(QnaReplyDTO qnaReplyDTO);

  QnaDetailMessageDTO getQnaDetails(int qnaIdx, String status, int userIdx);
}
