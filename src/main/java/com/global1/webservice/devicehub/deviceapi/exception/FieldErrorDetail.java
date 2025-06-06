package com.global1.webservice.devicehub.deviceapi.exception;

public class FieldErrorDetail {
    private String field;
    private String error;

    public FieldErrorDetail(String field, String error) {
        this.field = field;
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
