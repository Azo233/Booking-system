package com.booking.system.user_service.user_service.baseExceptions;

public class UserNotFoundException extends RuntimeException {

    private final String identifier;

    public UserNotFoundException(String identifier) {
        super(String.format("User not found with identifier: %s", identifier));
        this.identifier = identifier;
    }

    public UserNotFoundException(String identifier, String message) {
        super(message);
        this.identifier = identifier;
    }

    public UserNotFoundException(String identifier, String message, Throwable cause) {
        super(message, cause);
        this.identifier = identifier;
    }