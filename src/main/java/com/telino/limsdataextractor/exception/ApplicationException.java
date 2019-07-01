package com.telino.limsdataextractor.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ApplicationException extends RuntimeException {

    protected Map<String, Object> details = new HashMap<>();
    int codeErreur = 0;
    private HttpStatus httpStatus;

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, int codeErreur) {
        super(message);
        this.codeErreur = codeErreur;
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public ApplicationException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public int getCodeErreur() {
        return codeErreur;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}