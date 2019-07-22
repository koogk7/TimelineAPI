package com.d2.timeline.domain.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // Todo 알아보기
public enum ErrorCode {
    //Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    //Board
    UNMATCHED_WRITER(400, "B001", "Request is not writer"),

    //Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    AUTH_NOT_ALLOWED(400, "M003", "Role is not allowed"),
    AUTH_SIZE_OVER(400, "M004", "Count of Role is Only One"),

    //Relation
    UNMATCHED_REQUESTOR(400, "R001", "Requestor has not Authority"),
    NOT_EXIST_RELATION(400, "R002", "Not Exist Relation"),
    ;
    private final int status;
    private final String code;
    private final String message;


    ErrorCode(final int status, final String code, final String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}
