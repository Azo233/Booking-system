package com.booking.system.user_service.user_service.service;

import com.booking.system.user_service.user_service.baseExceptions.EmailAlreadyExistsException;
import com.booking.system.user_service.user_service.baseExceptions.InvalidPasswordException;
import com.booking.system.user_service.user_service.baseExceptions.UserNotFoundException;
import com.booking.system.user_service.user_service.dto.request.UserRegistrationRequestDto;
import com.booking.system.user_service.user_service.dto.request.UserUpdateRequestDto;
import com.booking.system.user_service.user_service.mapper.UserMapper;
import com.booking.system.user_service.user_service.model.User;
import com.booking.system.user_service.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordService passwordService;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordService passwordService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.userMapper = userMapper;
    }

    // If you add Kafka later
    // @Autowired
    // private KafkaEventService kafkaEventService;

    /**
     * Create user from registration DTO
     */
    public User createUser(UserRegistrationRequestDto registrationDto) {
        logger.info("Creating user with email: {}", registrationDto.getEmail());

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException(registrationDto.getEmail());
        }

        if (!passwordService.isPasswordStrong(registrationDto.getPassword())) {
            throw new InvalidPasswordException(
                    "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
            );
        }

        // Convert DTO to entity
        User user = userMapper.toUserEntity(registrationDto);

        // Hash password
        String hashedPassword = passwordService.hashPassword(registrationDto.getPassword());
        user.setPasswordHash(hashedPassword);

        // Save user
        User savedUser = userRepository.save(user);

        logger.info("User created successfully with ID: {}", savedUser.getId());

        // TODO: Publish Kafka event when you implement it
        // publishUserCreatedEvent(savedUser);

        return savedUser;
    }

    /**
     * Alternative: Create user with individual parameters (if you prefer this approach)
     */
    public User createUser(String firstName, String lastName, String email, String password, String phoneNumber) {
        logger.info("Creating user with email: {}", email);

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        // Validate password strength
        if (!passwordService.isPasswordStrong(password)) {
            throw new InvalidPasswordException(
                    "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
            );
        }

        // Create user entity
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        // Hash password
        String hashedPassword = passwordService.hashPassword(password);
        user.setPasswordHash(hashedPassword);

        // Save user
        User savedUser = userRepository.save(user);

        logger.info("User created successfully with ID: {}", savedUser.getId());

        // TODO: Publish Kafka event when you implement it
        // publishUserCreatedEvent(savedUser);

        return savedUser;
    }


    @Transactional
    public Optional<User> authenticateUser(String email, String password) {
        logger.debug("Authentication attempt for email: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            logger.warn("Authentication failed: User not found with email: {}", email);
            return Optional.empty();
        }

        User user = userOpt.get();

        // Verify password
        boolean passwordMatches = passwordService.verifyPassword(password, user.getPasswordHash());

        if (!passwordMatches) {
            logger.warn("Authentication failed: Invalid password for user: {}", user.getId());
            return Optional.empty();
        }

        logger.info("User {} authenticated successfully", user.getId());
        return Optional.of(user);
    }

    /**
     * Update user profile from DTO
     */
    public User updateUserProfile(Long userId, UserUpdateRequestDto updateDto) {
        logger.info("Updating user profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        // Update user from DTO
        userMapper.updateUserFromDto(user, updateDto);

        // Save updated user
        User updatedUser = userRepository.save(user);

        logger.info("User {} profile updated successfully", userId);

        // TODO: Publish Kafka event when you implement it
        // publishUserUpdatedEvent(updatedUser, changedFields);

        return updatedUser;
    }

    /**
     * Alternative: Update user profile with individual parameters
     */
    public User updateUserProfile(Long userId, String firstName, String lastName, String phoneNumber) {
        logger.info("Updating user profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        // Update fields
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);

        // Save updated user
        User updatedUser = userRepository.save(user);

        logger.info("User {} profile updated successfully", userId);

        // TODO: Publish Kafka event when you implement it
        // publishUserUpdatedEvent(updatedUser);

        return updatedUser;
    }

    /**
     * Change user password
     */
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        logger.info("Changing password for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        // Verify current password
        if (!passwordService.verifyPassword(currentPassword, user.getPasswordHash())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        // Validate new password strength
        if (!passwordService.isPasswordStrong(newPassword)) {
            throw new InvalidPasswordException(
                    "New password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
            );
        }

        // Hash and update password
        String hashedNewPassword = passwordService.hashPassword(newPassword);
        user.setPasswordHash(hashedNewPassword);

        userRepository.save(user);

        logger.info("Password changed successfully for user: {}", userId);

        // TODO: Publish Kafka event when you implement it
        // publishPasswordChangedEvent(user);
    }

    /**
     * Delete user
     */
    public void deleteUser(Long userId, Long adminId, String reason) {
        logger.info("Deleting user {} by admin {} with reason: {}", userId, adminId, reason);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        // Store user data for events before deletion
        String userEmail = user.getEmail();

        // Delete user
        userRepository.deleteById(userId);

        logger.info("User {} deleted successfully", userId);

        // TODO: Publish Kafka event when you implement it
        // publishUserDeletedEvent(userId, userEmail, adminId, reason);
    }

    /**
     * Find user by ID
     */
    @Transactional
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Transactional
    public Page<User> searchUsersByName(String searchTerm, Pageable pageable) {
        return userRepository.findByNameContaining(searchTerm, pageable);
    }

    @Transactional
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String resetPassword(Long userId, Long adminId) {
        logger.info("Resetting password for user {} by admin {}", userId, adminId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        // Generate temporary password
        String tempPassword = passwordService.generateRandomPassword();
        String hashedPassword = passwordService.hashPassword(tempPassword);

        user.setPasswordHash(hashedPassword);
        userRepository.save(user);

        logger.info("Password reset successfully for user: {}", userId);

        // TODO: Publish Kafka event when you implement it
        // publishPasswordResetEvent(user, adminId);

        return tempPassword; // Return plain password (to send via email, etc.)
    }

    /**
     * Soft delete - set status to INACTIVE instead of deleting
     */
    public User softDeleteUser(Long userId, Long adminId, String reason) {
        logger.info("Soft deleting user {} by admin {} with reason: {}", userId, adminId, reason);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        User updatedUser = userRepository.save(user);

        logger.info("User {} soft deleted (status changed to INACTIVE)", userId);

        // TODO: Publish Kafka event when you implement it
        // publishUserSoftDeletedEvent(updatedUser, adminId, reason);

        return updatedUser;
    }

    /**
     * Reactivate user
     */
    public User reactivateUser(Long userId, Long adminId) {
        logger.info("Reactivating user {} by admin {}", userId, adminId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        User reactivatedUser = userRepository.save(user);

        logger.info("User {} reactivated successfully", userId);

        // TODO: Publish Kafka event when you implement it
        // publishUserReactivatedEvent(reactivatedUser, adminId);

        return reactivatedUser;
    }

    // TODO: Add these methods when you implement Kafka
    /*
    private void publishUserCreatedEvent(User user) {
        UserCreatedEvent event = new UserCreatedEvent(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getStatus().toString()
        );
        kafkaEventService.publishUserCreatedEvent(event);
    }

    private void publishUserUpdatedEvent(User user) {
        // Implementation for user updated event
    }

    private void publishPasswordChangedEvent(User user) {
        // Implementation for password changed event
    }

    private void publishUserDeletedEvent(Long userId, String email, Long adminId, String reason) {
        // Implementation for user deleted event
    }
    */
}