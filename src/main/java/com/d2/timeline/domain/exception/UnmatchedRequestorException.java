package com.d2.timeline.domain.exception;

public class UnmatchedRequestorException extends BusinessException {

    public UnmatchedRequestorException(String message) {
        super(message, ErrorCode.UNMATCHED_REQUESTOR);
    }
}


