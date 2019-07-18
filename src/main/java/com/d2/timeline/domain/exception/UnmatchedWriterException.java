package com.d2.timeline.domain.exception;

public class UnmatchedWriterException extends BusinessException {

    public UnmatchedWriterException(String message) {
        super(message, ErrorCode.UNMATCHED_WRITER);
    }

}
