package com.desyd.api.exception;

public class ResourceNotFoundException extends CustomException{

    public ResourceNotFoundException(String message){
        super(message, "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resourceType, String identifier) {
        super(
                String.format("%s not found: %s", resourceType, identifier),
                "RESOURCE_NOT_FOUND"
        );
    }

    public ResourceNotFoundException(String resourceType, String identifier, String errorCode) {
        super(
                String.format("%s not found: %s", resourceType, identifier),
                errorCode
        );
    }
}
