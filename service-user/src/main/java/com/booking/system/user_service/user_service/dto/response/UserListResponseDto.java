package com.booking.system.user_service.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Paginated user list response")
public class UserListResponseDto {

    @Schema(description = "List of users")
    private List<UserResponseDto> users;

    @Schema(description = "Pagination information")
    private PaginationDto pagination;

    public UserListResponseDto() {}

    public UserListResponseDto(List<UserResponseDto> users, PaginationDto pagination) {
        this.users = users;
        this.pagination = pagination;
    }

    // Getters and Setters
    public List<UserResponseDto> getUsers() { return users; }
    public void setUsers(List<UserResponseDto> users) { this.users = users; }

    public PaginationDto getPagination() { return pagination; }
    public void setPagination(PaginationDto pagination) { this.pagination = pagination; }
}
