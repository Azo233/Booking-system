package com.booking.system.user_service.user_service.baseExceptions;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an InvalidPasswordException with a default message about password requirements.
     */
    public static InvalidPasswordException withDefaultMessage() {
        return new InvalidPasswordException(
                "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        );
    }
}
