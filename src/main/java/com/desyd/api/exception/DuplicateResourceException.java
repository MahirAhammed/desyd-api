package com.desyd.api.exception;

public class DuplicateResourceException extends CustomException {

    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }

    public DuplicateResourceException(String resourceType, String field, String value) {
        super(
                String.format("%s with %s '%s' already exists", resourceType, field, value),
                "DUPLICATE_RESOURCE"
        );
    }
}
