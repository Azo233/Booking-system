package com.booking.system.user_service.user_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User login request")
public class LoginRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User's password", example = "SecurePass123!", required = true)
    private String password;

    public LoginRequestDto() {}

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "email='" + email + '\'' +
                '}'; // Note: Password excluded from toString for security
    }
}
