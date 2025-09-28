package com.example.OnlinePay.exception;

import com.example.OnlinePay.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handlePaymentException(AppException ex) {
        ErrorCode error = ex.getErrorCode();
        ApiResponse<?> response = ApiResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build();
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(500).body(response);
    }
}
