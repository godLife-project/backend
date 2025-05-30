package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.contents.QnaReplyDTO;
import com.godLife.project.dto.list.QnaDetailDTO;
import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.qnaWebsocket.QnaReplyListDTO;
import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.MatchedListMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.QnaDetailMessageDTO;
import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminInfos;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminList;
import com.godLife.project.dto.statistics.response.ResponseQnaAdminStat;
import com.godLife.project.enums.MessageStatus;
import com.godLife.project.enums.QnaRedisKey;
import com.godLife.project.enums.QnaStatus;
import com.godLife.project.enums.WSDestination;
import com.godLife.project.exception.CustomException;
import com.godLife.project.exception.WebSocketBusinessException;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.impl.websocketImpl.WebSocketMessageService;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import com.godLife.project.service.interfaces.QnaMatchService;
import com.godLife.project.service.interfaces.QnaService;
import com.godLife.project.service.interfaces.UserService;
import com.godLife.project.service.interfaces.statistics.ServiceAdminStatService;
import com.godLife.project.utils.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QnaServiceImpl implements QnaService {

  private final QnaMapper qnaMapper;
  private final RedisService redisService;
  private final WebSocketMessageService messageService;
  private final UserService userService;
  private final ServiceAdminService serviceAdminService;
  private final QnaMatchService qnaMatchService;
  private final ServiceAdminStatService adminStatService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createQna(QnaDTO qnaDTO) {
    try {
      qnaMapper.createQna(qnaDTO); // 문의 저장
      redisService.leftPushToRedisQueue(QnaRedisKey.QNA_QUEUE_KEY.getKey(), String.valueOf(qnaDTO.getQnaIdx())); // 큐에 저장
      log.info("QnaService - createQna :: 문의 등록 후 큐에 추가됨 - {}", qnaDTO.getQnaIdx());

      // 관리자에게 전송할 객체 생성 및 데이터 전송
      List<QnaWaitListDTO> waitItem = qnaMapper.getWaitSingleQna(qnaDTO.getQnaIdx());

      WaitListMessageDTO waitQna = new WaitListMessageDTO();
      waitQna.setWaitQnA(waitItem);
      waitQna.setStatus(MessageStatus.ADD.getStatus());

      // 관리자에게 실시간 대기중 문의 리스트 전송
      messageService.sendToAll(WSDestination.ALL_WAIT_QNA_LIST.getDestination(), waitQna);

    } catch (Exception e) {
      log.error("QnaService - createQna :: 1:1 문의 저장 중 오류 발생: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw new CustomException("DB 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // 대기중인 문의 리스트 조회 (전체)
  @Override
  public WaitListMessageDTO getlistAllWaitQna(String status, String username) {
    try {
      List<QnaWaitListDTO> waitList = qnaMapper.getlistAllWaitQna();
      WaitListMessageDTO request = new WaitListMessageDTO();
      request.setWaitQnA(waitList);
      request.setStatus(status);

      return request;
    } catch (Exception e) {
      throw new WebSocketBusinessException("대기중인 QnA 리스트를 불러오는 중 오류 발생", 5001, username);
    }
  }

  // 매칭된 문의 리스트 조회 (개인)
  @Override
  public MatchedListMessageDTO getlistAllQnaByFindNotStatus(int adminIdx, String status, List<String> notStatus, String username) {
    try {
      List<QnaMatchedListDTO> matchedList = qnaMapper.getlistAllMatchedQna(adminIdx, notStatus);
      //System.out.println(matchedList);
      MatchedListMessageDTO request = new MatchedListMessageDTO();
      request.setMatchedQnA(matchedList);
      request.setStatus(status);

      return request;
    } catch (Exception e) {
      log.error("QnaService - getlistAllQnaByFindNotStatus :: 서버 오류 발생", e);
      throw new WebSocketBusinessException("QnA 리스트를 불러오는 중 오류 발생", 5001, username);
    }
  }

  // 매칭된 단일 문의 리스트 조회 (개인)
  @Override
  public MatchedListMessageDTO getMatchedSingleQna(int adminIdx, int qnaIdx, String status, List<String> notStatus, String username) {
    try {
      List<QnaMatchedListDTO> matchedQnaDTO = qnaMapper.getMatchedSingleQna(adminIdx, qnaIdx, notStatus);

      if (matchedQnaDTO != null) {
        MatchedListMessageDTO request = new MatchedListMessageDTO();
        request.setMatchedQnA(matchedQnaDTO);
        request.setStatus(status);

        return request;
      }

      return null;
    } catch (Exception e) {
      throw new WebSocketBusinessException("매칭된 QnA 리스트를 불러오는 중 오류 발생", 5001, username);
    }
  }

  // qna 본문 조회
  @Override
  public String getQnaContent(int qnaIdx) {
    try {
      String rawContent = qnaMapper.getQnaContent(qnaIdx, QnaStatus.DELETED.getStatus());

      if (rawContent == null || rawContent.isBlank()) {
        throw new CustomException("조회하려는 문의가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
      }

      return HtmlSanitizer.sanitize(rawContent);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("QnaService - getQnaContent :: 문의 본문 조회 중 오류 발생: ", e);
      throw new CustomException("DB 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // 문의 답변
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void commentReply(QnaReplyDTO qnaReplyDTO) {
    try {
      // 문의 인덱스 저장
      int qnaParentIdx = qnaReplyDTO.getQnaIdx();

      // 문의 존재 여부와 매칭 여부 검증
      QnaDTO qnaParent = qnaValidator(qnaParentIdx);

      // 유저 검증
      boolean isWriter = whoAreYou(qnaParent, qnaReplyDTO.getUserIdx());

      // 답변 저장
      qnaMapper.commentReply(qnaReplyDTO);

      String setStatus = QnaStatus.RESPONDING.getStatus();

      List<String> notStatus = new ArrayList<>();
      notStatus.add(QnaStatus.WAIT.getStatus());
      notStatus.add(QnaStatus.COMPLETE.getStatus());
      notStatus.add(QnaStatus.DELETED.getStatus());

      // 관리자 ID 추출
      String username = userService.getUserIdByUserIdx(qnaParent.getAUserIdx());

      String whichQnA = redisService.getStringData(QnaRedisKey.QNA_WATCHER.getKey() + username);
      boolean isWatched = String.valueOf(qnaParentIdx).equals(whichQnA);

      // 답변 수 증가 (조회 시 초기화 돼야 함. --> 알림용)
      // 유저가 추가 문의 작성 시 관리자가 해당 문의를 안 보고 있을 때
      // Q_COUNT 증가 및 관리자에게 전송
      if (!isWriter) {
        // 관리자가 작성 중일 경우 → 무조건 실행
        qnaMapper.increaseReplyCount(false, qnaParentIdx, setStatus, notStatus);

        // 첫 답변을 달 경우 실행되는 로직
        if (qnaParent.getRespondingDate() == null) {
          qnaMapper.setRespondingDate(qnaParentIdx);
        }

        String findStatus = QnaStatus.CONNECT.getStatus();
        qnaMapper.setQnaStatus(qnaParentIdx, QnaStatus.RESPONDING.getStatus(), Collections.singletonList(findStatus));

        MatchedListMessageDTO matchedResponse = getMatchedSingleQna(
            qnaParent.getAUserIdx(),
            qnaParent.getQnaIdx(),
            MessageStatus.UPDATE.getStatus(),
            notStatus,
            username
        );
        messageService.sendToUser(username, WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), matchedResponse);

        // 상담원이 응답했음을 레디스에 저장
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String value = qnaParent.getAUserIdx() + "_" + formatted;
        redisService.saveStringData(QnaRedisKey.QNA_ADMIN_ANSWERED.getKey() + qnaParentIdx, value, 's', 10);
      } else {
        // 유저가 작성 중일 경우
        if (!isWatched) {
          // 관리자가 보고 있지 않을 때만 실행
          if (qnaParent.getQnaStatus().equals(QnaStatus.CONNECT.getStatus())) {
            setStatus = QnaStatus.CONNECT.getStatus();
          }
          qnaMapper.increaseReplyCount(true, qnaParentIdx, setStatus, notStatus);

          MatchedListMessageDTO matchedResponse = getMatchedSingleQna(
              qnaParent.getAUserIdx(),
              qnaParent.getQnaIdx(),
              MessageStatus.UPDATE.getStatus(),
              notStatus,
              username
          );
          messageService.sendToUser(username, WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), matchedResponse);
        }
      }

      if (isWatched) {
        // 방금 작성한 답변 정보 조회
        QnaDetailMessageDTO detailInfos = new QnaDetailMessageDTO();
        detailInfos.setQnaIdx(qnaParentIdx);
        detailInfos.setStatus(MessageStatus.ADD_COMMENT.getStatus());

        QnaReplyListDTO comment = qnaMapper.getRecentComment(qnaParentIdx);
        comment.setUserIdx(0); // 유저 인덱스 조회 안되도록 초기화
        comment.setContent(HtmlSanitizer.sanitize(comment.getContent())); // 댓글 필터링

        detailInfos.setComments(List.of(comment));

        messageService.sendToUser(username, WSDestination.QUEUE_QNA_DETAIL.getDestination() + qnaParentIdx, detailInfos);
      }

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("QnaService - commentReply :: 답변 작성 중 에러 발생 :", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw new CustomException("답변 작성 중 예상치 못한 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * 문의가 존재하는지 확인 후, 매칭 여부를 검증합니다.
   * 모두 통과 시 해당 문의의 기본 정보를 리턴합니다.
   * @param qnaIdx 검증을 시도 할 문의의 인덱스 번호
   * @return QnaDTO
   */
  private QnaDTO qnaValidator(int qnaIdx) {
    QnaDTO qnaValidate = qnaMapper.getQnaInfosByQnaIdx(qnaIdx, QnaStatus.DELETED.getStatus());

    // 문의 존재 유무 확인
    if (qnaValidate == null) {
      log.info("QnaService - commentReply :: 답변 작성 중 문의가 존재하지 않아 작성을 취소합니다. ### 취소된 문의Idx ::> {}", qnaIdx);
      throw new CustomException("해당 문의는 삭제 되었거나 존재 하지 않습니다.", HttpStatus.NOT_FOUND);
    }
    // 문의 매칭 여부 확인
    String status = qnaValidate.getQnaStatus();

    if (status.equals(QnaStatus.WAIT.getStatus())) {
      log.info("QnaService - commentReply :: 답변 작성 중 상담원과 연결 되지 않아 작성을 취소합니다. ### 취소된 문의Idx ::> {}", qnaIdx);
      throw new CustomException("아직 상담원과 연결되지 않아 답변을 달 수 없습니다.", HttpStatus.PRECONDITION_FAILED);
    }
    if (status.equals(QnaStatus.COMPLETE.getStatus())) {
      log.info("QnaService - commentReply :: 완료 처리 된 문의는 답변을 작성할 수 없습니다. ### 취소된 문의Idx ::> {}", qnaIdx);
      throw new CustomException("응답 완료 상태의 문의는 추가 답변이 불가합니다. " +
          "추가 질문이 있을 경우 문의를 새로 작성해주세요.", HttpStatus.PRECONDITION_FAILED);
    }

    return qnaValidate;
  }

  /**
   * 문의 작성자가 답변을 다는지, 답변자가 답변을 다는지 bool 값으로 구분합니다.
   * @param qnaDTO 비교할 문의 대상입니다.
   * @param userIdx 어떤 유저가 답변을 작성하는지 나타냅니다.
   * @return bool :: True-유저 / False-관리자
   */
  private boolean whoAreYou(QnaDTO qnaDTO, int userIdx) {

    if (qnaDTO.getQUserIdx() == userIdx) { // 문의 작성자 (유저) 인가?
      // 작성자 맞음
      return true;

    } else if (qnaDTO.getAUserIdx() == userIdx) { // 작성자 아니면 답변자(매칭된 관리자) 인가?
      // 답변자 맞음
      return false;

    } else { // 둘 다 아니면 작성 못함
      throw new CustomException("작성자 혹은 담당 관리자가 아닙니다", HttpStatus.FORBIDDEN);
    }
  }

  // 문의 상세 조회
  @Override
  @Transactional(rollbackFor = RuntimeException.class)
  public QnaDetailMessageDTO getQnaDetails(int qnaIdx, String status, int userIdx) {
    QnaDetailMessageDTO detailInfos = new QnaDetailMessageDTO();
    detailInfos.setQnaIdx(qnaIdx);
    detailInfos.setStatus(status);

    try {
      QnaDTO forValidate = qnaMapper.getQnaInfosByQnaIdx(qnaIdx, QnaStatus.DELETED.getStatus());
      // 부모 문의 존재 유무 검증
      if (forValidate == null) {
        throw new CustomException("존재하지 않거나, 삭제된 문의 입니다.", HttpStatus.NOT_FOUND);
      }
      // 부모 문의 작성자/관리자 검증
      boolean isWriter = whoAreYou(forValidate, userIdx);

      String qnaBody = qnaMapper.getQnaContent(qnaIdx, QnaStatus.DELETED.getStatus());
      if (qnaBody == null || qnaBody.isBlank()) {
        throw new CustomException("존재하지 않거나, 삭제된 문의 입니다.", HttpStatus.NOT_FOUND);
      }
      detailInfos.setBody(HtmlSanitizer.sanitize(qnaBody)); // 본문 필터링 후 저장

      List<QnaReplyListDTO> tempReply = qnaMapper.getQnaReplyByQnaIdx(qnaIdx);
      List<QnaReplyListDTO> filteredReply = new ArrayList<>();

      for (QnaReplyListDTO reply : tempReply) {
        reply.setContent(HtmlSanitizer.sanitize(reply.getContent())); // 답변 필터링 후 세팅
        filteredReply.add(reply);
      }
      detailInfos.setComments(filteredReply); // 답변 불러오기
      qnaMapper.setClearReplyCountByQnaIdx(qnaIdx, isWriter); // 누적 답변 개수 초기화

      return detailInfos;

    } catch (CustomException e) {
      log.info("QnaService - getQnaDetails :: {} ### HttpStatus : {}", e.getMessage(), e.getStatus());
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw e;

    } catch (Exception e) {
      log.error("QnaService - getQnaDetails :: 예상치 못한 서버 오류가 발생했습니다 :", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw new CustomException("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // 상세 조회 시 추가 정보 제공 (유저용)
  @Override
  public QnaDetailDTO setQnaDetailForUser(QnaDetailMessageDTO base) {
    try {
      if (base == null) {
        throw new CustomException("QnaService - setQnaDetailForUser :: 문의 상세 정보가 null 입니다.", HttpStatus.NOT_FOUND);
      }

      QnaDetailDTO temp = qnaMapper.getQnaExtraDetailByQnaIdx(base.getQnaIdx());

      QnaDetailDTO response = new QnaDetailDTO(base);
      response.setTitle(temp.getTitle());
      response.setCreatedAt(temp.getCreatedAt());
      response.setModifiedAt(temp.getModifiedAt());
      response.setCategory(temp.getCategory());
      response.setQnaStatus(temp.getQnaStatus());

      return response;

    } catch (CustomException e) {
      log.info(e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("QnaService - setQnaDetailForUser :: 서버 오류가 발생 했습니다.", e);
      throw e;
    }
  }

  @Override
  @Transactional(rollbackFor = RuntimeException.class)
  public void modifyQnA(QnaDTO modifyDTO, List<String> setStatus) {
    try {
      int editorIdx = modifyDTO.getQUserIdx();
      int qnaIdx = modifyDTO.getQnaIdx();

      QnaDTO forValidate = qnaMapper.getQnaInfosByQnaIdx(qnaIdx, QnaStatus.DELETED.getStatus());

      if (forValidate == null) {
        throw new CustomException("존재하지 않거나, 삭제된 문의 입니다.", HttpStatus.NOT_FOUND);
      }
      if (!(QnaStatus.WAIT.getStatus().equals(forValidate.getQnaStatus()) || QnaStatus.CONNECT.getStatus().equals(forValidate.getQnaStatus()))) {
        throw new CustomException("대기중 혹은 연결된(관리자가 답변을 하지 않은) 상태의 문의만 수정 가능합니다.", HttpStatus.PRECONDITION_FAILED);
      }
      if (editorIdx != forValidate.getQUserIdx()) {
        throw new CustomException("문의 작성자만 수정 가능합니다.", HttpStatus.FORBIDDEN);
      }

      // 문의 수정 진행
      int result = qnaMapper.modifyQnA(modifyDTO, setStatus);

      if (result == 0) {
        throw new CustomException("수정할 문의가 없어 문의 수정을 취소합니다.", HttpStatus.NO_CONTENT);
      }

      if (forValidate.getAUserIdx() != 0) {
        // 관리자 ID 추출
        String username = userService.getUserIdByUserIdx(forValidate.getAUserIdx());

        List<String> notStatus = new ArrayList<>();
        notStatus.add(QnaStatus.WAIT.getStatus());
        notStatus.add(QnaStatus.COMPLETE.getStatus());
        notStatus.add(QnaStatus.DELETED.getStatus());

        MatchedListMessageDTO matchedResponse = getMatchedSingleQna(
            forValidate.getAUserIdx(),
            forValidate.getQnaIdx(),
            MessageStatus.UPDATE.getStatus(),
            notStatus,
            username
        );

        messageService.sendToUser(username, WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), matchedResponse);

        String whichQnA = redisService.getStringData(QnaRedisKey.QNA_WATCHER.getKey() + username);
        boolean isWatched = String.valueOf(qnaIdx).equals(whichQnA);

        if (isWatched) {
          // 방금 수정한 본문 전송
          QnaDetailMessageDTO detailInfos = new QnaDetailMessageDTO();
          detailInfos.setQnaIdx(qnaIdx);
          detailInfos.setStatus(MessageStatus.MODIFY_BODY.getStatus());

          detailInfos.setBody(HtmlSanitizer.sanitize(modifyDTO.getContent()));

          messageService.sendToUser(username, WSDestination.QUEUE_QNA_DETAIL.getDestination() + qnaIdx, detailInfos);
        }

      } else {
        // 모든 관리자에게 전송할 객체 생성 및 데이터 전송
        List<QnaWaitListDTO> waitItem = qnaMapper.getWaitSingleQna(forValidate.getQnaIdx());

        WaitListMessageDTO waitQna = new WaitListMessageDTO();
        waitQna.setWaitQnA(waitItem);
        waitQna.setStatus(MessageStatus.UPDATE.getStatus());

        // 관리자에게 실시간 대기중 문의 리스트 전송
        messageService.sendToAll(WSDestination.ALL_WAIT_QNA_LIST.getDestination(), waitQna);
      }
    } catch (CustomException e) {
      log.info("QnaService - modifyQnA :: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("QnaService - modifyQnA :: 예기치 않은 서버 오류가 발생했습니다.", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw e;
    }
  }

  // 답변 수정
  @Override
  public void modifyReply(QnaReplyDTO modifyReplyDTO) {
    try {
      int qnaReplyIdx = modifyReplyDTO.getQnaReplyIdx();
      int qnaIdx = modifyReplyDTO.getQnaIdx();
      int userIdx = modifyReplyDTO.getUserIdx();

      QnaDTO forValidate = qnaMapper.getQnaInfosByQnaIdx(qnaIdx, QnaStatus.DELETED.getStatus());
      if (forValidate == null) {
        throw new CustomException("존재하지 않거나, 삭제된 문의 입니다.", HttpStatus.NOT_FOUND);
      }
      if (!(QnaStatus.RESPONDING.getStatus().equals(forValidate.getQnaStatus()) || QnaStatus.SLEEP.getStatus().equals(forValidate.getQnaStatus()))) {
        throw new CustomException("응답 상태의 문의만 수정 가능합니다.", HttpStatus.PRECONDITION_FAILED);
      }

      // 부모 문의 작성자/관리자 검증
      log.info("답변 수정자 : {} - {}", userIdx, whoAreYou(forValidate, userIdx)?"문의작성자(유저)":"담당관리자");

      // 가장 최근 답변 조회
      QnaReplyListDTO recentComment = qnaMapper.getRecentComment(qnaIdx);

      if (recentComment.getQnaReplyIdx() != qnaReplyIdx) {
        throw new CustomException("이미 답변이 달렸을 경우 수정할 수 없습니다.", HttpStatus.PRECONDITION_FAILED);
      }
      if (recentComment.getUserIdx() != userIdx) {
        throw new CustomException("답변 수정은 작성자 본인만 가능합니다.", HttpStatus.FORBIDDEN);
      }

      // 답변 수정
      qnaMapper.modifyReply(modifyReplyDTO);

      // 관리자 ID 추출
      String username = userService.getUserIdByUserIdx(forValidate.getAUserIdx());
      String whichQnA = redisService.getStringData(QnaRedisKey.QNA_WATCHER.getKey() + username);
      boolean isWatched = String.valueOf(qnaIdx).equals(whichQnA);

      if (isWatched) {
        // 방금 수정한 답변을 조회중인 관리자에게 전송
        QnaDetailMessageDTO detailInfos = new QnaDetailMessageDTO();
        detailInfos.setQnaIdx(qnaIdx);
        detailInfos.setStatus(MessageStatus.MODIFY_COMM.getStatus());

        QnaReplyListDTO comment = qnaMapper.getRecentComment(qnaIdx);
        comment.setUserIdx(0); // 유저 인덱스 조회 안되도록 초기화
        comment.setContent(HtmlSanitizer.sanitize(comment.getContent())); // 댓글 필터링

        detailInfos.setComments(List.of(comment));

        messageService.sendToUser(username, WSDestination.QUEUE_QNA_DETAIL.getDestination() + qnaIdx, detailInfos);
      }
    } catch (CustomException e) {
      log.info("QnaService - modifyReply :: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("QnaService - modifyReply :: 답변 수정 중 예기치 않은 오류 발생", e);
       throw new CustomException("답변 수정 중 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void deleteQna(int qnaIdx, int userIdx) {

    try {
      QnaDTO forValidate = qnaMapper.getQnaInfosByQnaIdx(qnaIdx, QnaStatus.DELETED.getStatus());

      if (forValidate == null) {
        throw new CustomException("존재하지 않거나, 이미 삭제된 문의 입니다.", HttpStatus.NOT_FOUND);
      }
      if (forValidate.getQUserIdx() != userIdx) {
        throw new CustomException("작성자가 아닙니다.", HttpStatus.FORBIDDEN);
      }

      int adminIdx = forValidate.getAUserIdx();

      qnaMapper.deleteQna(qnaIdx, userIdx);

      String qnaStatus = forValidate.getQnaStatus();
      // 문의가 대기, 완료 상태가 아닌 경우
      if (!(qnaStatus.equals(QnaStatus.WAIT.getStatus()) || qnaStatus.equals(QnaStatus.COMPLETE.getStatus()))) {

        String username = userService.getUserIdByUserIdx(adminIdx);

        // 매칭된 관리자에게 최신화
        MatchedListMessageDTO deleteMatchedQna = new MatchedListMessageDTO();
        deleteMatchedQna.setStatus(MessageStatus.REMOVE.getStatus());
        deleteMatchedQna.setMatchedQnA(Collections.singletonList(
            new QnaMatchedListDTO() {{
              setQnaIdx(qnaIdx);
            }}
        ));
        // 데이터 전송
        messageService.sendToUser(username, WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), deleteMatchedQna);
        // 매칭 수 재설정
        serviceAdminService.refreshMatchCount(adminIdx);
      } else if (qnaStatus.equals(QnaStatus.WAIT.getStatus())){
        // 대기중인 리스트에서 삭제 할 문의 설정
        WaitListMessageDTO deleteWaitQna = new WaitListMessageDTO();
        deleteWaitQna.setStatus(MessageStatus.REMOVE.getStatus());
        deleteWaitQna.setWaitQnA(Collections.singletonList(
            new QnaWaitListDTO() {{
              setQnaIdx(qnaIdx);
            }}
        ));
        // 데이터 전송
        messageService.sendToAll(WSDestination.ALL_WAIT_QNA_LIST.getDestination(), deleteWaitQna);
      }
    } catch (CustomException e) {
      log.info("QnaService - deleteQna :: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("QnaService - deleteQna :: ", e);
      throw e;
    }
  }

  @Override
  public void deleteReply(int qnaIdx, int qnaReplyIdx, int userIdx) {
    try {
      QnaDTO forValidate = qnaMapper.getQnaInfosByQnaIdx(qnaIdx, QnaStatus.DELETED.getStatus());

      if (forValidate == null) {
        throw new CustomException("존재하지 않거나, 삭제된 문의 입니다.", HttpStatus.NOT_FOUND);
      }
      if (QnaStatus.COMPLETE.getStatus().equals(forValidate.getQnaStatus()) || QnaStatus.DELETED.getStatus().equals(forValidate.getQnaStatus())) {
        throw new CustomException("완료 혹은 삭제된 문의의 답변은 삭제 할 수 없습니다.", HttpStatus.PRECONDITION_FAILED);
      }

      // 부모 문의 작성자/관리자 검증
      log.info("답변 삭제 요청 : {} - {}", userIdx, whoAreYou(forValidate, userIdx)?"문의작성자(유저)":"담당관리자");

      // 가장 최근 답변 조회
      QnaReplyListDTO recentComment = qnaMapper.getRecentComment(qnaIdx);

      if (recentComment == null) {
        throw new CustomException("현재 문의에 존재하는 답변이 없습니다.", HttpStatus.NO_CONTENT);
      }
      if (recentComment.getQnaReplyIdx() != qnaReplyIdx) {
        throw new CustomException("이미 답변이 달렸거나 삭제된 답변입니다.", HttpStatus.PRECONDITION_FAILED);
      }
      if (recentComment.getUserIdx() != userIdx) {
        throw new CustomException("답변 삭제는 작성자 본인만 가능합니다.", HttpStatus.FORBIDDEN);
      }

      // 답변 수정
      qnaMapper.deleteReply(qnaReplyIdx, userIdx);

      // 관리자 ID 추출
      if (!forValidate.getQnaStatus().equals(QnaStatus.WAIT.getStatus())) {
        String username = userService.getUserIdByUserIdx(forValidate.getAUserIdx());
        String whichQnA = redisService.getStringData(QnaRedisKey.QNA_WATCHER.getKey() + username);
        boolean isWatched = String.valueOf(qnaIdx).equals(whichQnA);

        if (isWatched) {
          // 방금 수정한 답변을 조회중인 관리자에게 전송
          QnaReplyListDTO comment = new QnaReplyListDTO();
          comment.setQnaReplyIdx(qnaReplyIdx);

          QnaDetailMessageDTO detailInfos = new QnaDetailMessageDTO();
          detailInfos.setQnaIdx(qnaIdx);
          detailInfos.setStatus(MessageStatus.DELETE_COMM.getStatus());
          detailInfos.setComments(Collections.singletonList(comment));

          messageService.sendToUser(username, WSDestination.QUEUE_QNA_DETAIL.getDestination() + qnaIdx, detailInfos);
        }
      }
    } catch (CustomException e) {
      log.info("QnaService - deleteReply :: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("QnaService - deleteReply :: 예기치 못한 에러가 발생 했습니다.", e);
      throw e;
    }
  }

  @Override
  public void setQnaStatus(int qnaIdx, Integer userIdx, String setStatus, List<String> findStatus) {
    try {
      // 문의 상태 수정 전 원본 조회
      QnaDTO forValidate = qnaMapper.getQnaInfosByQnaIdx(qnaIdx, QnaStatus.DELETED.getStatus());

      // 부모 문의 존재 유무 검증
      if (forValidate == null) {
        throw new CustomException("존재하지 않거나, 삭제된 문의 입니다.", HttpStatus.NOT_FOUND);
      }

      // 원본의 상태값이 findStatus 와 부합하는지 검증
      if (!findStatus.contains(forValidate.getQnaStatus())) {
        log.error("QnaService - setQnaStatus :: status 검증 불일치 [원본 status : {} ↔ 검증 satus : {}]", forValidate.getQnaStatus(), findStatus);
        throw new CustomException("상태를 변경할 문의의 상태값이 부합하지 않습니다.", HttpStatus.UNPROCESSABLE_ENTITY);
      }

      // userIdx 가 있을 경우, 클라이언트의 요청으로 데이터 수정하는것으로 간주
      // 수정 권한 있는지 검증
      if (userIdx != null) {
        boolean isWriter = whoAreYou(forValidate, userIdx);
        log.info("{} - {} 의 요청으로 문의의 상태값을 {} 으로 변경합니다.", isWriter ? "작성자" : "상담원", userIdx, setStatus);
      }

      // 모든 검증 통과 시 문의 상태값 변경
      qnaMapper.setQnaStatus(qnaIdx, setStatus, findStatus);

      int adminIdx = forValidate.getAUserIdx();
      // 매칭 리스트에 완료 된 문의 삭제 요청을 위해 아이디 조회
      String adminId = userService.getUserIdByUserIdx(adminIdx);

      switch (setStatus) {
        case "COMPLETE" :
          // 메세지 패키징
          MatchedListMessageDTO matchedListQnAMessage = qnaMatchService.setMatchedListForMessage(qnaIdx,MessageStatus.REMOVE.getStatus());
          // 메세지 전송
          messageService.sendToUser(adminId, WSDestination.QUEUE_MATCHED_QNA_LIST.getDestination(), matchedListQnAMessage);


          // 상담원 명단 매칭수 최신화 하기
          serviceAdminService.refreshMatchCount(adminIdx);
          List<ServiceCenterAdminInfos> accessAdminInfos = serviceAdminService.getAllAccessServiceAdminList();
          List<ServiceCenterAdminList> accessAdminList = serviceAdminService.getAccessAdminListForMessage(accessAdminInfos);

          messageService.sendToAll(WSDestination.ALL_ACCESS_ADMIN_LIST.getDestination(), accessAdminList);

          // 완료 처리된 문의 완료 리스트 최신화 하기
          List<String> notStatus = new ArrayList<>();
          notStatus.add(QnaStatus.WAIT.getStatus());
          notStatus.add(QnaStatus.CONNECT.getStatus());
          notStatus.add(QnaStatus.RESPONDING.getStatus());
          notStatus.add(QnaStatus.SLEEP.getStatus());
          notStatus.add(QnaStatus.DELETED.getStatus());

          MatchedListMessageDTO completeQnA = getMatchedSingleQna(adminIdx, qnaIdx, MessageStatus.ADD.getStatus(), notStatus, adminId);
          if (completeQnA != null) {
            // 통계 데이터 최신화
            ResponseQnaAdminStat qnaAdminStat = adminStatService.updateQnaAdminSummaryStats(qnaIdx, adminIdx);
            messageService.sendToUser(adminId, WSDestination.QUEUE_QNA_ADMIN_STATISTICS.getDestination(), qnaAdminStat);
            messageService.sendToUser(adminId, WSDestination.QUEUE_COMPLETE_QNA_LIST.getDestination(), completeQnA);
          }
          break;
        case "WAIT" :
          // 로직 추가시 구현할 것
          break;
        case "CONNECT" :
          // 로직 추가시 구현할 것
          break;
        case "RESPONDING" :
          // 로직 추가시 구현할 것
          break;
        case "SLEEP" :
          // 로직 추가시 구현할 것
          break;
        default:
          throw new CustomException("사용하지 않는 문의의 상태값 입니다.", HttpStatus.CONFLICT);
      }


    } catch (CustomException e) {
      log.error("QnaService - setQnaStatus :: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("QnaService - setQnaStatus :: 예기치 못한 에러가 발생 했습니다.", e);
      throw e;
    }
  }


}
