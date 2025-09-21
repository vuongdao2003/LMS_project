package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED (1001,"User existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002,"Username must be at least 5 characters",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003,"Uncategorized error",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004,"User not exitsed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005,"Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006,"Unauthorized",HttpStatus.FORBIDDEN),;

    private int code;
    private String message;
    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code =code;
        this.message=message;
        this.httpStatusCode=httpStatus;
    }
    private HttpStatusCode httpStatusCode;
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
