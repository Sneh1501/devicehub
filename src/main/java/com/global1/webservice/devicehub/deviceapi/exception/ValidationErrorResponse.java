package com.global1.webservice.devicehub.deviceapi.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private List<FieldErrorDetail> errors;

    public ValidationErrorResponse(LocalDateTime timestamp, int status, String message, List<FieldErrorDetail> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldErrorDetail> errors) {
        this.errors = errors;
    }
}

