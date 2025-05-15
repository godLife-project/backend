package com.godLife.project.exception;

import com.godLife.project.dto.list.QnaListDTO;

import java.util.List;

public class QnaCategoryDeletePendingException extends RuntimeException {
    private final List<QnaListDTO> qnaList;

    public QnaCategoryDeletePendingException(String message, List<QnaListDTO> qnaList) {
        super(message);
        this.qnaList = qnaList;
    }

    public List<QnaListDTO> getQnaList() {
        return qnaList;
    }
}
