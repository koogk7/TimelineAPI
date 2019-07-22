package com.d2.timeline.domain.exception;

public class LoginInvalidException extends BusinessException {
    public LoginInvalidException(String message) {
        super(message, ErrorCode.LOGIN_INVALID);
    }
}
