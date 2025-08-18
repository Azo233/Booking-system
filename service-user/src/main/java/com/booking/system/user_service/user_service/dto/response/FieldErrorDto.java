package com.booking.system.user_service.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Field validation error")
public class FieldErrorDto {

    @Schema(description = "Name of the field that failed validation", example = "email")
    private String field;

    @Schema(description = "Invalid value that was provided", example = "invalid-email")
    private Object rejectedValue;

    @Schema(description = "Validation error message", example = "Please provide a valid email address")
    private String message;

    @Schema(description = "Validation error code", example = "INVALID_EMAIL_FORMAT")
    private String errorCode;

    public FieldErrorDto() {}

    public FieldErrorDto(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public FieldErrorDto(String field, Object rejectedValue, String message, String errorCode) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public Object getRejectedValue() { return rejectedValue; }
    public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    @Override
    public String toString() {
        return "FieldErrorDto{" +
                "field='" + field + '\'' +
                ", rejectedValue=" + rejectedValue +
                ", message='" + message + '\'' +
                '}';
    }
}