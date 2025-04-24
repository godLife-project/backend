package com.godLife.project.mapper;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.contents.QnaReplyDTO;
import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.qnaWebsocket.QnaReplyListDTO;
import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QnaMapper {

  // 1:1 문의 작성
  void createQna(QnaDTO qnaDTO);

  // 'WAIT' 상태 문의의 qnaIdx 가져오기
  @Select("SELECT QNA_IDX FROM QNA_TABLE WHERE QNA_STATUS = 'WAIT'")
  List<Integer> getlistWaitQnaIdx();

  // 'WAIT' 상태 문의의 리스트용 정보 가져오기
  List<QnaWaitListDTO> getlistAllWaitQna();
  // 'WAIT' 상태 단일 문의 리스트용 정보 가져오기
  List<QnaWaitListDTO> getWaitSingleQna(int qnaIdx);

  // 매칭된 문의 리스트 조회
  List<QnaMatchedListDTO> getlistAllMatchedQna(int adminIdx);

  // 매칭된 단일 문의 리스트 조회 (개인)
  List<QnaMatchedListDTO> getMatchedSingleQna(int adminIdx, int qnaIdx);

  // 문의 본문 조회
  @Select("SELECT CONTENT FROM QNA_TABLE WHERE QNA_IDX = #{qnaIdx} AND QNA_STATUS != #{status}")
  String getQnaContent(int qnaIdx, String status);

  // 답변 달기
  void commentReply(QnaReplyDTO qnaReplyDTO);

  // 방금 작성한 답변 가져오기
  @Select("SELECT * FROM QNA_REPLY WHERE QNA_REPLY_IDX = #{qnaReplyIdx}")
  QnaReplyListDTO getRecentComment(int qnaReplyIdx);

  /**
   * 문의 검증용 기초 정보 조회
   * @param qnaIdx 조회 할 문의의 인덱스 번호
   * @param status 제외 할 문의 상태
   * @return qnaIdx, qUserIdx, aUserIdx, qnaStatus
   * @code qnaIdx : int
   * @code qUserIdx : int
   * @code aUserIdx : int
   * @code qnaStatus : String
   */
  @Select("SELECT QNA_IDX, Q_USER_IDX, A_USER_IDX, QNA_STATUS FROM QNA_TABLE WHERE QNA_IDX = #{qnaIdx} AND QNA_STATUS != #{status}")
  QnaDTO getQnaInfosByQnaIdx(int qnaIdx, String status);

  /**
   * 작성자 혹은 답변자(관리자) 에게 알림용으로 사용 할 A_COUNT, Q_COUNT 의 값을 증가시킵니다.
   * @param isWriter isWriter 를 통해 작성자 / 답변자 를 구분합니다.
   * @param qnaIdx 어떤 문의를 변경할 지 사용 합니다.
   * @param setStatus 답변이 달렸다 => 아직 응대중 이라는 뜻으로 RESPONDING 상태로 업데이트 합니다.
   * @param notStatus 삭제/완료 된 문의는 수정 할 수 없으므로 제약을 걸어두기 위한 파라미터 입니다.
   */
  void increaseReplyCount(@Param("isWriter") boolean isWriter,
                          @Param("qnaIdx") int qnaIdx,
                          @Param("setStatus") String setStatus,
                          @Param("notStatus") List<String> notStatus);

  /**
   * <strong>선택한 1:1 문의에 달려있는 답변을 모두 조회합니다.</strong>
   * <p>답변 작성일 기준으로 오름차순으로 정렬 됩니다.</p>
   * @param qnaIdx 조회할 문의의 인덱스 번호
   * @return {@code List<QnaReplyListDTO>}
   */
  List<QnaReplyListDTO> getQnaReplyByQnaIdx(int qnaIdx);

  /**
   * <strong>원하는 문의의 Q_COUNT 혹은 A_COUNT 를 0으로 초기화 합니다.</strong>
   * <p>{@code isWriter} 를 통해 어느 것을 초기화 할 지 결정합니다.</p>
   * <p>
   * {@code True} : A_COUNT 초기화 <br/>
   * {@code False} : C_COUNT 초기화
   * </p>
   * @param qnaIdx 초기화 할 문의의 인덱스 번호
   * @param isWriter 무엇을 초기화 할 지 정해 줄 친구
   */
  void setClearReplyCountByQnaIdx(int qnaIdx, boolean isWriter);

  /**
   * <strong>1:1 문의 (QnA) 수정</strong>
   * <p>{@code qnaIdx}, {@code title}, {@code content}, {@code category} 를 필수로 받아야 합니다.</p>
   * <p>{@code qUserIdx} 는 jwt토큰에서 추출한 userIdx 를 넣어줘야 합니다.</p>
   * <p>{@code exceptStatus} 에 제외 할 문의의 상태값을 지정합니다.</p>
   * @param modifyDTO 삭제할 문의 DTO -> {@code QnaDTO}
   * @param setStatus 조회 할 문의 상태 값 List
   * @return {@code int} - 수정 성공한 행의 개수
   */
  int modifyQnA(@Param("modifyDTO") QnaDTO modifyDTO, @Param("setStatus") List<String> setStatus);

}
