package com.booking.system.user_service.user_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Success response wrapper")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponseDto<T> {

    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Success message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response data")
    private T data;

    @Schema(description = "Response metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Response timestamp", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public SuccessResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    public SuccessResponseDto(int status, String message, T data) {
        this();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public SuccessResponseDto(int status, String message, T data, Map<String, Object> metadata) {
        this(status, message, data);
        this.metadata = metadata;
    }

    public static <T> SuccessResponseDto<T> ok(T data) {
        return new SuccessResponseDto<>(200, "Success", data);
    }

    public static <T> SuccessResponseDto<T> ok(String message, T data) {
        return new SuccessResponseDto<>(200, message, data);
    }

    public static <T> SuccessResponseDto<T> created(T data) {
        return new SuccessResponseDto<>(201, "Resource created successfully", data);
    }

    public static <T> SuccessResponseDto<T> created(String message, T data) {
        return new SuccessResponseDto<>(201, message, data);
    }

    public static SuccessResponseDto<Void> noContent(String message) {
        return new SuccessResponseDto<>(204, message, null);
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
