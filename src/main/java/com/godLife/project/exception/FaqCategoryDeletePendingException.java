package com.godLife.project.exception;

import com.godLife.project.dto.contents.FaQDTO;

import java.util.List;

public class FaqCategoryDeletePendingException extends RuntimeException{
    private final List<FaQDTO> pendingFaqList;

    public FaqCategoryDeletePendingException(String message, List<FaQDTO> pendingFaqList) {
        super(message);
        this.pendingFaqList = pendingFaqList;
    }

    public List<FaQDTO> getPendingFaqList() {
        return pendingFaqList;
    }
}
