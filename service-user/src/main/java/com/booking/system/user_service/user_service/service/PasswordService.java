package com.booking.system.user_service.user_service.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    public boolean isPasswordHashed(String password) {
        return password != null && password.startsWith("$2a$");
    }
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        logger.debug("Hashing password (length: {})", plainPassword.length());

        String hashedPassword = passwordEncoder.encode(plainPassword);

        logger.debug("Password hashed successfully");
        return hashedPassword;
    }

    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase, lowercase, and digit
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);

        return hasUpper && hasLower && hasDigit;
    }

    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}
