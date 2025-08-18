package com.booking.system.user_service.user_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User statistics")
public class UserStatsResponseDto {

    @Schema(description = "Total number of users", example = "1250")
    private long totalUsers;

    @Schema(description = "Number of active users", example = "1100")
    private long activeUsers;

    @Schema(description = "Number of inactive users", example = "125")
    private long inactiveUsers;

    @Schema(description = "Number of suspended users", example = "25")
    private long suspendedUsers;

    @Schema(description = "Number of users registered today", example = "15")
    private long usersToday;

    @Schema(description = "Number of users registered this week", example = "89")
    private long usersThisWeek;

    @Schema(description = "Number of users registered this month", example = "234")
    private long usersThisMonth;

    public UserStatsResponseDto() {}

    public UserStatsResponseDto(long totalUsers, long activeUsers, long inactiveUsers, long suspendedUsers) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.inactiveUsers = inactiveUsers;
        this.suspendedUsers = suspendedUsers;
    }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getActiveUsers() { return activeUsers; }
    public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }

    public long getInactiveUsers() { return inactiveUsers; }
    public void setInactiveUsers(long inactiveUsers) { this.inactiveUsers = inactiveUsers; }

    public long getSuspendedUsers() { return suspendedUsers; }
    public void setSuspendedUsers(long suspendedUsers) { this.suspendedUsers = suspendedUsers; }

    public long getUsersToday() { return usersToday; }
    public void setUsersToday(long usersToday) { this.usersToday = usersToday; }

    public long getUsersThisWeek() { return usersThisWeek; }
    public void setUsersThisWeek(long usersThisWeek) { this.usersThisWeek = usersThisWeek; }

    public long getUsersThisMonth() { return usersThisMonth; }
    public void setUsersThisMonth(long usersThisMonth) { this.usersThisMonth = usersThisMonth; }
}