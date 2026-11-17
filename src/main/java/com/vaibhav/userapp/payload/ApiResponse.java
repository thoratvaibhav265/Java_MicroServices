package com.vaibhav.userapp.payload;

import org.springframework.http.HttpStatus;

public class ApiResponse {

    private String message;
    private Boolean success;
    private HttpStatus status;

    public ApiResponse() {
    }

    public ApiResponse(String message, Boolean success, HttpStatus status) {
        this.message = message;
        this.success = success;
        this.status = status;
    }

    public static ApiResponseBuilder builder() {
        return new ApiResponseBuilder();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public static class ApiResponseBuilder {
        private String message;
        private Boolean success;
        private HttpStatus status;

        ApiResponseBuilder() {
        }

        public ApiResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder success(Boolean success) {
            this.success = success;
            return this;
        }

        public ApiResponseBuilder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(message, success, status);
        }
    }
}
