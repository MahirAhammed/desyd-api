package com.desyd.api.exception;

public class AuthenticationException extends CustomException{

    public AuthenticationException(String message){
        super(message, "AUTHENTICATION_FAILED");
    }

    public AuthenticationException(String message, String errorCode){
        super(message, errorCode);
    }
}
