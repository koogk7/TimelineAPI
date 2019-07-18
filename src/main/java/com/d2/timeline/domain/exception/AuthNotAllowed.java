package com.d2.timeline.domain.exception;

public class AuthNotAllowed extends BusinessException {
    public AuthNotAllowed(String message) {
        super(message, ErrorCode.AUTH_NOT_ALLOWED);
    }
}
