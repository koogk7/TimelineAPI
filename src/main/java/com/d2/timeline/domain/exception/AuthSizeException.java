package com.d2.timeline.domain.exception;

public class AuthSizeException extends BusinessException{
    public AuthSizeException(String message) {
        super(message, ErrorCode.AUTH_SIZE_OVER);
    }
}
