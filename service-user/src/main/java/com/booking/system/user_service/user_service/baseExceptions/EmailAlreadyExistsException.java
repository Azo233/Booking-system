package com.booking.system.user_service.user_service.baseExceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    private final String email;

    public EmailAlreadyExistsException(String email) {
        super(String.format("Email '%s' is already registered in the system", email));
        this.email = email;
    }

    public EmailAlreadyExistsException(String email, String message) {
        super(message);
        this.email = email;
    }

    public EmailAlreadyExistsException(String email, String message, Throwable cause) {
        super(message, cause);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

}
