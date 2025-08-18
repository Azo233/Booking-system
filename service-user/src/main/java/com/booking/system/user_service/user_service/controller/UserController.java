package com.booking.system.user_service.user_service.controller;

import com.booking.system.user_service.user_service.dto.request.LoginRequestDto;
import com.booking.system.user_service.user_service.dto.request.PasswordChangeRequestDto;
import com.booking.system.user_service.user_service.dto.request.UserRegistrationRequestDto;
import com.booking.system.user_service.user_service.dto.request.UserUpdateRequestDto;
import com.booking.system.user_service.user_service.dto.response.*;
import com.booking.system.user_service.user_service.mapper.UserMapper;
import com.booking.system.user_service.user_service.model.User;
import com.booking.system.user_service.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users in the booking system")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * Register a new user
     */
    @Operation(summary = "Register new user",
            description = "Create a new user account with encrypted password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegistrationRequestDto registrationDto,
            HttpServletRequest request) {

        try {
            logger.info("User registration attempt for email: {}", registrationDto.getEmail());

            User user = userService.createUser(
                    registrationDto.getFirstName(),
                    registrationDto.getLastName(),
                    registrationDto.getEmail(),
                    registrationDto.getPassword(),
                    registrationDto.getPhoneNumber()
            );

            UserResponseDto userResponse = userMapper.toUserResponseDto(user);

            logger.info("User registered successfully with ID: {}", user.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessResponseDto.created("User registered successfully", userResponse));

        } catch (IllegalArgumentException e) {
            logger.warn("User registration failed: {}", e.getMessage());

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.BAD_REQUEST.value(),
                    "REGISTRATION_FAILED",
                    e.getMessage(),
                    "Please check your input and try again",
                    request.getRequestURI()
            );

            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Unexpected error during user registration", e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "INTERNAL_ERROR",
                    "Registration failed due to server error",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Authenticate user (login)
     */
    @Operation(summary = "User login",
            description = "Authenticate user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @Valid @RequestBody LoginRequestDto loginDto,
            HttpServletRequest request) {

        try {
            logger.info("Login attempt for email: {}", loginDto.getEmail());

            Optional<User> userOpt = userService.authenticateUser(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Create successful authentication response
                AuthenticationResponseDto response = userMapper.toAuthenticationResponseDto(
                        user, null, null // Token can be added later when implementing JWT
                );

                logger.info("User {} logged in successfully", user.getId());
                return ResponseEntity.ok(response);

            } else {
                logger.warn("Failed login attempt for email: {}", loginDto.getEmail());

                ErrorResponseDto error = new ErrorResponseDto(
                        HttpStatus.UNAUTHORIZED.value(),
                        "INVALID_CREDENTIALS",
                        "Invalid email or password",
                        "Please check your credentials and try again",
                        request.getRequestURI()
                );

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

        } catch (Exception e) {
            logger.error("Unexpected error during login", e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "LOGIN_ERROR",
                    "Login failed due to server error",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all users with pagination
     */
    @Operation(summary = "Get all users",
            description = "Retrieve a paginated list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) int size,

            @Parameter(description = "Sort field", example = "lastName")
            @RequestParam(defaultValue = "lastName") String sortBy,

            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,

            @Parameter(description = "Filter by status", example = "ACTIVE")
            @RequestParam(required = false) String status,

            HttpServletRequest request) {

        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;

            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<User> userPage;

            if (status != null && !status.trim().isEmpty()) {
                UserStatus userStatus = userMapper.stringToUserStatus(status);
                userPage = userService.findUsersByStatus(userStatus, pageable);
            } else {
                userPage = userService.findAllUsers(pageable);
            }

            UserListResponseDto response = userMapper.toUserListResponseDto(userPage);

            return ResponseEntity.ok(SuccessResponseDto.ok("Users retrieved successfully", response));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request parameters: {}", e.getMessage());

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.BAD_REQUEST.value(),
                    "INVALID_PARAMETERS",
                    "Invalid request parameters",
                    e.getMessage(),
                    request.getRequestURI()
            );

            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error retrieving users", e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "RETRIEVAL_ERROR",
                    "Failed to retrieve users",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get user by ID
     */
    @Operation(summary = "Get user by ID",
            description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            HttpServletRequest request) {

        try {
            Optional<User> userOpt = userService.findById(userId);

            if (userOpt.isPresent()) {
                UserResponseDto userResponse = userMapper.toUserResponseDto(userOpt.get());
                return ResponseEntity.ok(SuccessResponseDto.ok("User found", userResponse));
            } else {
                ErrorResponseDto error = new ErrorResponseDto(
                        HttpStatus.NOT_FOUND.value(),
                        "USER_NOT_FOUND",
                        "User not found",
                        "No user exists with ID: " + userId,
                        request.getRequestURI()
                );

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

        } catch (Exception e) {
            logger.error("Error retrieving user with ID: {}", userId, e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "RETRIEVAL_ERROR",
                    "Failed to retrieve user",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Update user profile
     */
    @Operation(summary = "Update user profile",
            description = "Update user's profile information (excluding email and password)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,

            @Valid @RequestBody UserUpdateRequestDto updateDto,
            HttpServletRequest request) {

        try {
            User updatedUser = userService.updateUserProfile(
                    userId,
                    updateDto.getFirstName(),
                    updateDto.getLastName(),
                    updateDto.getPhoneNumber()
            );

            UserResponseDto userResponse = userMapper.toUserResponseDto(updatedUser);

            logger.info("User {} updated successfully", userId);

            return ResponseEntity.ok(SuccessResponseDto.ok("User updated successfully", userResponse));

        } catch (IllegalArgumentException e) {
            logger.warn("Failed to update user {}: {}", userId, e.getMessage());

            HttpStatus status = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            String errorCode = e.getMessage().contains("not found") ?
                    "USER_NOT_FOUND" : "UPDATE_FAILED";

            ErrorResponseDto error = new ErrorResponseDto(
                    status.value(),
                    errorCode,
                    e.getMessage(),
                    "Please check your input and try again",
                    request.getRequestURI()
            );

            return ResponseEntity.status(status).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error updating user {}", userId, e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "UPDATE_ERROR",
                    "Failed to update user",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Change user password
     */
    @Operation(summary = "Change password",
            description = "Change user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid password data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{userId}/password")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,

            @Valid @RequestBody PasswordChangeRequestDto passwordDto,
            HttpServletRequest request) {

        try {
            // Validate password confirmation
            if (!passwordDto.isPasswordConfirmed()) {
                ErrorResponseDto error = new ErrorResponseDto(
                        HttpStatus.BAD_REQUEST.value(),
                        "PASSWORD_MISMATCH",
                        "Password confirmation does not match",
                        "Please ensure both password fields match",
                        request.getRequestURI()
                );

                return ResponseEntity.badRequest().body(error);
            }

            userService.changePassword(
                    userId,
                    passwordDto.getCurrentPassword(),
                    passwordDto.getNewPassword()
            );

            logger.info("Password changed successfully for user: {}", userId);

            return ResponseEntity.ok(SuccessResponseDto.ok("Password changed successfully", null));

        } catch (IllegalArgumentException e) {
            logger.warn("Failed to change password for user {}: {}", userId, e.getMessage());

            HttpStatus status = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            String errorCode = e.getMessage().contains("not found") ?
                    "USER_NOT_FOUND" : "PASSWORD_CHANGE_FAILED";

            ErrorResponseDto error = new ErrorResponseDto(
                    status.value(),
                    errorCode,
                    e.getMessage(),
                    "Please check your input and try again",
                    request.getRequestURI()
            );

            return ResponseEntity.status(status).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error changing password for user {}", userId, e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "PASSWORD_CHANGE_ERROR",
                    "Failed to change password",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Delete user account
     */
    @Operation(summary = "Delete user",
            description = "Delete a user account (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,

            @Parameter(description = "Admin user ID performing the deletion", required = true)
            @RequestParam Long adminId,

            @Parameter(description = "Reason for deletion")
            @RequestParam(required = false, defaultValue = "Account deleted by admin") String reason,

            HttpServletRequest request) {

        try {
            userService.deleteUser(userId, adminId, reason);

            logger.info("User {} deleted by admin {}", userId, adminId);

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            logger.warn("Failed to delete user {}: {}", userId, e.getMessage());

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.NOT_FOUND.value(),
                    "USER_NOT_FOUND",
                    e.getMessage(),
                    "No user exists with the specified ID",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error deleting user {}", userId, e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "DELETION_ERROR",
                    "Failed to delete user",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Search users by email
     */
    @Operation(summary = "Find user by email",
            description = "Search for a user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid email format",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/search/email/{email}")
    public ResponseEntity<?> findUserByEmail(
            @Parameter(description = "Email address to search", required = true, example = "john.doe@example.com")
            @PathVariable @Email String email,
            HttpServletRequest request) {

        try {
            Optional<User> userOpt = userService.findByEmail(email);

            if (userOpt.isPresent()) {
                UserResponseDto userResponse = userMapper.toUserResponseDto(userOpt.get());
                return ResponseEntity.ok(SuccessResponseDto.ok("User found", userResponse));
            } else {
                return ResponseEntity.ok(SuccessResponseDto.ok("No user found with email: " + email, null));
            }

        } catch (Exception e) {
            logger.error("Error searching for user by email: {}", email, e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "SEARCH_ERROR",
                    "Failed to search for user",
                    "Please try again later",
                    request.getRequestURI()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get user statistics
     */
    @Operation(summary = "Get user statistics",
            description = "Retrieve statistics about users")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponseDto.class)))
    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats() {
        try {
            UserStatsResponseDto stats = userService.getUserStatistics();
            return ResponseEntity.ok(SuccessResponseDto.ok("Statistics retrieved successfully", stats));

        } catch (Exception e) {
            logger.error("Error retrieving user statistics", e);

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "STATS_ERROR",
                    "Failed to retrieve statistics",
                    "Please try again later",
                    "/api/v1/users/stats"
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
