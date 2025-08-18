package com.booking.system.user_service.user_service.mapper;


import com.booking.system.user_service.user_service.dto.request.UserRegistrationRequestDto;
import com.booking.system.user_service.user_service.dto.request.UserUpdateRequestDto;
import com.booking.system.user_service.user_service.dto.response.*;
import com.booking.system.user_service.user_service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /**
     * Convert User entity to UserResponseDto
     */
    public UserResponseDto toUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


    public User toUserEntity(UserRegistrationRequestDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }


    public void updateUserFromDto(User user, UserUpdateRequestDto dto) {
        if (user == null || dto == null) {
            return;
        }

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        // Note: Email and password are not updated through this method for security
    }

    public UserListResponseDto toUserListResponseDto(Page<User> userPage) {
        if (userPage == null) {
            return new UserListResponseDto();
        }

        List<UserResponseDto> userDtos = userPage.getContent()
                .stream()
                .map(this::toUserResponseDto)
                .collect(Collectors.toList());

        PaginationDto pagination = new PaginationDto(
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.isFirst(),
                userPage.isLast(),
                userPage.hasNext(),
                userPage.hasPrevious()
        );

        return new UserListResponseDto(userDtos, pagination);
    }


    public List<UserResponseDto> toUserResponseDtoList(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::toUserResponseDto)
                .collect(Collectors.toList());
    }


    public AuthenticationResponseDto toAuthenticationResponseDto(User user, String token, Long expiresIn) {
        UserResponseDto userDto = toUserResponseDto(user);

        if (token != null) {
            return new AuthenticationResponseDto(
                    "SUCCESS",
                    "Authentication successful",
                    userDto,
                    token,
                    expiresIn
            );
        } else {
            return new AuthenticationResponseDto(
                    "SUCCESS",
                    "Authentication successful",
                    userDto
            );
        }
    }


    public AuthenticationResponseDto toFailedAuthenticationResponseDto(String message) {
        return new AuthenticationResponseDto(
                "FAILED",
                message != null ? message : "Authentication failed",
                null
        );
    }


    public UserStatsResponseDto toUserStatsResponseDto(
            long totalUsers, long activeUsers, long inactiveUsers, long suspendedUsers,
            long usersToday, long usersThisWeek, long usersThisMonth) {

        UserStatsResponseDto stats = new UserStatsResponseDto(
                totalUsers, activeUsers, inactiveUsers, suspendedUsers
        );

        stats.setUsersToday(usersToday);
        stats.setUsersThisWeek(usersThisWeek);
        stats.setUsersThisMonth(usersThisMonth);

        return stats;
    }

    public UserResponseDto toMinimalUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public UserResponseDto toPublicUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }
}

