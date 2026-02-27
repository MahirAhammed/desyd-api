package com.desyd.api.exception;

public class ConflictException extends CustomException{

    public ConflictException(String message) {
        super(message, "ALREADY_EXISTS");
    }

    public ConflictException(String message, String errorCode) {
        super(message, errorCode);
    }
}
