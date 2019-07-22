package com.d2.timeline.domain.exception;

public class NotExistRelationException extends BusinessException {
    public NotExistRelationException(String message){
        super(message, ErrorCode.NOT_EXIST_RELATION);
    }
}
