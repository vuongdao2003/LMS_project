package com.example.OnlinePay.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(99, "Unknown error: ", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED (1001,"User existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002,"Username must be at least 5 characters",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003,"Uncategorized error",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004,"User not exitsed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005,"Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006,"Unauthorized",HttpStatus.FORBIDDEN),


    // Payment error codes
    INVALID_PAYMENT(2001, "Invalid payment request", HttpStatus.BAD_REQUEST),
    PAYMENT_TIMEOUT(2002, "Timeout or no response from gateway", HttpStatus.GATEWAY_TIMEOUT),
    INVALID_SIGNATURE(2003, "Invalid security signature", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(01, "Order Not Found", HttpStatus.NOT_FOUND),
    INVALID_AMOUNT(04, "Invalid Amount", HttpStatus.BAD_REQUEST),
    ORDER_EXPIRED(02, "Order already confirmed", HttpStatus.BAD_REQUEST),
    INVALID_CHECKSUM(97, "Invalid checksum", HttpStatus.BAD_REQUEST),


    VNP_INVALID_TMN_CODE(02, "Invalid terminal code (TmnCode)", HttpStatus.BAD_REQUEST),
    VNP_INVALID_FORMAT(03, "Invalid request format", HttpStatus.BAD_REQUEST),
    VNP_ORDER_NOT_FOUND(91, "Transaction not found", HttpStatus.NOT_FOUND),
    VNP_DUPLICATE_REQUEST(94, "Duplicate request within restricted timeframe", HttpStatus.BAD_REQUEST),
    VNP_TRANSACTION_FAILED(95, "Transaction failed at VNPAY, refund request rejected", HttpStatus.BAD_REQUEST),
    VNP_INVALID_CHECKSUM(97, "Invalid checksum", HttpStatus.BAD_REQUEST),
    VNP_OTHER_ERROR(99, "Other errors", HttpStatus.INTERNAL_SERVER_ERROR)
    ;



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
