package com.desyd.api.exception;

public class UnauthorizedException extends CustomException{

    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}
