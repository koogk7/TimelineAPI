package com.d2.timeline.domain.exception;

public class EmailDuplicationException extends BusinessException {
    public EmailDuplicationException(String message){
        super(message,ErrorCode.EMAIL_DUPLICATION);
    }
}
