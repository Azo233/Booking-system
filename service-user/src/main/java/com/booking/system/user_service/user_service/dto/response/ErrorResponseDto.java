package com.booking.system.user_service.user_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Error response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error code for client handling", example = "VALIDATION_FAILED")
    private String errorCode;

    @Schema(description = "Human-readable error message", example = "Validation failed for the request")
    private String message;

    @Schema(description = "Detailed error description", example = "The provided email address is already in use")
    private String details;

    @Schema(description = "API path where the error occurred", example = "/api/v1/users")
    private String path;

    @Schema(description = "Error timestamp", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Request ID for tracking", example = "abc123-def456-ghi789")
    private String requestId;

    @Schema(description = "Field-specific validation errors")
    private List<FieldErrorDto> fieldErrors;

    public ErrorResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDto(int status, String errorCode, String message) {
        this();
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorResponseDto(int status, String errorCode, String message, String details, String path) {
        this(status, errorCode, message);
        this.details = details;
        this.path = path;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public List<FieldErrorDto> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldErrorDto> fieldErrors) { this.fieldErrors = fieldErrors; }

    @Override
    public String toString() {
        return "ErrorResponseDto{" +
                "status=" + status +
                ", errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
