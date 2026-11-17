package com.vaibhav.userapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaibhav.userapp.payload.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse> handleNoSuchElementException(NoSuchElementException exception) {
        return errorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleInvalidRequest(MethodArgumentNotValidException exception) {
        FieldError error = exception.getBindingResult().getFieldError("email");
        if (error == null) {
            error = exception.getBindingResult().getFieldError();
        }
        String message = error == null ? "Request validation failed." : error.getDefaultMessage();
        return errorResponse(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<ApiResponse> errorResponse(HttpStatus status, String message) {
        ApiResponse response = ApiResponse.builder()
                .message(message)
                .success(false)
                .status(status)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
