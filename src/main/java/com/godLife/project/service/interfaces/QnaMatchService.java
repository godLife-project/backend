package com.godLife.project.service.interfaces;

import com.godLife.project.dto.qnaWebsocket.listMessage.WaitListMessageDTO;
import com.godLife.project.dto.serviceAdmin.AdminIdxAndIdDTO;

public interface QnaMatchService {

    /**
     * 대기 상태의 단일 문의를 특정 관리자에게 할당 합니다.
     * @param qnaIdx 할당할 문의의 인덱스
     * @param adminIdx 할당 받을 관리자의 인덱스 번호
     * @return bool : 할당 될 경우 true 반환
     */
    boolean matchSingleQna(int qnaIdx, int adminIdx);

    /**
     * 현재 매칭할 수 있는 관리자 중 가장 여유 있는 관리자 의 ID와 IDX 를 가져옵니다.
     * @return AdminIdxAndIdDTO
     */
    AdminIdxAndIdDTO getAdminInfo();

    /**
     * STOMP 응답 메시지 패키징 메서드 ::
     * 대기중 문의 리스트 최신화를 위해 DTO를 패키징 합니다.
     * @param qnaIdx 추가,삭제,업데이트 할 문의의 인덱스 번호
     * @param status 어떤 행동을 할 지 나타낼 상태값
     * @return WaitListMessageDTO
     */
    WaitListMessageDTO setWaitListForMessage(int qnaIdx, String status);
}
