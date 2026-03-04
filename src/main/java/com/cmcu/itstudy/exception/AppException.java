package com.cmcu.itstudy.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final String message;

    public AppException(String message) {
        super(message);
        this.message = message;
    }
}