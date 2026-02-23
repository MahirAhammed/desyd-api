package com.desyd.api.exception;

public abstract class CustomException extends RuntimeException{

    private final String errorCode;

    public CustomException(String message) {
        super(message);
        this.errorCode = this.getClass().getSimpleName();
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = this.getClass().getSimpleName();
    }

    public String getErrorCode() {
        return errorCode;
    }
}
