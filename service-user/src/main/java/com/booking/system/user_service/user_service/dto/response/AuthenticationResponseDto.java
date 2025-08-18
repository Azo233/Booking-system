package com.booking.system.user_service.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response")
public class AuthenticationResponseDto {

    @Schema(description = "Authentication status", example = "SUCCESS")
    private String status;

    @Schema(description = "Status message", example = "Login successful")
    private String message;

    @Schema(description = "User information")
    private UserResponseDto user;

    @Schema(description = "Authentication token (if using JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token expiration time in seconds", example = "3600")
    private Long expiresIn;

    public AuthenticationResponseDto() {}

    public AuthenticationResponseDto(String status, String message, UserResponseDto user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public AuthenticationResponseDto(String status, String message, UserResponseDto user, String token, Long expiresIn) {
        this.status = status;
        this.message = message;
        this.user = user;
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserResponseDto getUser() { return user; }
    public void setUser(UserResponseDto user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
}
