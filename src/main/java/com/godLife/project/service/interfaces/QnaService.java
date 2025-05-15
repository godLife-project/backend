package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.contents.QnaReplyDTO;
import com.godLife.project.dto.list.QnaDetailDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.MatchedListMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.QnaDetailMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;

import java.util.List;

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
  MatchedListMessageDTO getlistAllQnaByFindNotStatus(int adminIdx, String status, List<String> notStatus, String username);

  /**
   * 특정 문의의 정보를 조회 합니다. 특정 문의가 파라미터로 입력 받은 관리자의 것인지
   * 확인하고, 맞을 경우 STATUS 파라미터를 통해 응답 메세지의 상태를 지정하고,
   * WEBSOCKET 으로 전달할 DTO로 포장해줍니다.
   * @param adminIdx 검증하기 위한 관리자 인덱스 번호
   * @param qnaIdx 조회하기 위한 문의 인덱스 번호
   * @param status 클라이언트에게 보여 줄 상태 값
   * @return MatchedListMessageDTO
   */
  MatchedListMessageDTO getMatchedSingleQna(int adminIdx, int qnaIdx, String status, String username);

  /**
   * 현재 'WAIT' 상태인 문의의 전체 리스트를 조회합니다.
   * STATUS 파라미터를 통해 응답 메세지의 상태를 지정하고,
   * WEBSOCKET 으로 전달할 DTO로 포장해줍니다.
   * @param status 클라이언트에게 보여 줄 상태 값
   * @return WaitListMessageDTO
   */
  WaitListMessageDTO getlistAllWaitQna(String status, String username);

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

  /**
   * <strong>문의 상세 보기 메서드 입니다.</strong>
   * <p>선택한 문의의 본문과 그 문의에 달린 답변들을 모두 가져옵니다.</p>
   * @param qnaIdx 조회할 문의의 인덱스 번호
   * @param status 메세지 전송시 메세지 상태 값
   * @param userIdx 상세보기 요청한 유저의 인덱스 번호
   * @return {@code QnaDetailMessageDTO}
   */
  QnaDetailMessageDTO getQnaDetails(int qnaIdx, String status, int userIdx);

  /**
   * <strong>유저용 문의 상세 보기를 위한 메서드 입니다.</strong>
   * <p>유저용 문의 상세 보기는 리스트 조회시 제공된 정보까지 주기 위해 동작합니다.</p>
   * @param base {@code QnaDetailMessageDTO} 입니다. 문의 상세 정보가 담겨 있습니다.
   * @return {@code QnaDetailDTO} 문의 상세 조회 메서드를 통해 얻은 정보  + 리스트 정보까지 담아줍니다.
   */
  QnaDetailDTO setQnaDetailForUser(QnaDetailMessageDTO base);

  /**
   * <strong>1:1 문의(QnA)를 수정하기 위한 서비스 로직입니다.</strong>
   * <p>{@code qnaIdx}, {@code title}, {@code content}, {@code category} 를 필수로 받아야 합니다.</p>
   * <p>{@code qUserIdx} 는 jwt토큰에서 추출한 userIdx 를 넣어줘야 합니다.</p>
   * <p>{@code exceptStatus} 에 제외 할 문의의 상태값을 지정합니다.</p>
   * @param modifyDTO 삭제할 문의 DTO -> {@code QnaDTO}
   * @param setStatus 조회 할 문의 상태 값 List
   */
  void modifyQnA(QnaDTO modifyDTO, List<String> setStatus);

  /**
   * <strong>답변을 수정하기 위한 서비스 로직입니다.</strong>
   * <p>{@code qnaReplyIdx}, {@code qnaIdx}, {@code content} 를 필수로 받아야 합니다.</p>
   * <p>{@code userIdx} 는 jwt토큰에서 추출한 userIdx 를 넣어줘야 합니다.</p>
   * <p>내부적으로 검증을 진행 후, 모두 통과할 경우 답변을 수정한 후 담당 관리자 페이지에 바로 최신화 해줍니다.</p>
   * @param modifyReplyDTO 수정할 답변 DTO -> {@code qnaReplyDTO}
   */
  void modifyReply(QnaReplyDTO modifyReplyDTO);

  /**
   * <strong>문의 삭제 로직</strong>
   * <p>이미 삭제되었거나, 이미 완료된 문의는 삭제할 수 없습니다.</p>
   * <p>문의는 DB에서 완전히 삭제되는게 아닌, qnaStatus 컬럼만 DELETED 상태로 전환됩니다.</p>
   * @param qnaIdx 삭제할 문의의 인덱스 번호
   * @param userIdx 삭제하려는 유저의 인덱스 번호
   */
  void deleteQna(int qnaIdx, int userIdx);

  /**
   * <strong>답변 삭제 로직</strong>
   * <p>대기, 완료, 삭제 된 문의에 대한 답변은 삭제할 수 없습니다.</p>
   * <p>가장 마지막에 남긴 답변만 삭제 가능합니다.</p>
   * <p>1 -> 2 -> 3 순으로 답변이 달렸을 경우, 3 만 삭제 가능하고,
   * 3이 삭제 되면 그 후 2 를 삭제할 수 있습니다.</p>
   * <p>답변은 DB에서 완전히 삭제됩니다.</p>
   * @param qnaIdx 답변을 삭제할 문의의 인덱스 번호
   * @param qnaReplyIdx 삭제할 답변의 인덱스 번호
   * @param userIdx 삭제하려는 유저의 인덱스 번호
   */
  void deleteReply(int qnaIdx, int qnaReplyIdx, int userIdx);

  void setQnaStatus(int qnaIdx, Integer userIdx, String setStatus, List<String> findStatus);
}
