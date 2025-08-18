package com.booking.system.user_service.user_service.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Password change request")
public class PasswordChangeRequestDto {

    @NotBlank(message = "Current password is required")
    @Schema(description = "User's current password", required = true)
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 100, message = "New password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "New password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    @Schema(description = "New password (min 8 chars, uppercase, lowercase, number, special char)",
            example = "NewSecurePass123!", required = true)
    private String newPassword;

    @NotBlank(message = "Password confirmation is required")
    @Schema(description = "Confirmation of the new password", required = true)
    private String confirmPassword;

    public PasswordChangeRequestDto() {
    }

    public PasswordChangeRequestDto(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Validation method
    @Schema(hidden = true)
    public boolean isPasswordConfirmed() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }

    @Override
    public String toString() {
        return "PasswordChangeRequestDto{currentPassword='[HIDDEN]', newPassword='[HIDDEN]', confirmPassword='[HIDDEN]'}";
    }
}
