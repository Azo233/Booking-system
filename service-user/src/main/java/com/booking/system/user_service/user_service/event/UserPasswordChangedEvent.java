package com.booking.system.user_service.user_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPasswordChangedEvent extends BaseUserEvent {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("changedBy")
    private String changedBy; // "USER" or "ADMIN"

    @JsonProperty("ipAddress")
    private String ipAddress;

    public UserPasswordChangedEvent() {
        super("USER_PASSWORD_CHANGED");
    }

    public UserPasswordChangedEvent(Long userId, String email, String changedBy, String ipAddress) {
        super("USER_PASSWORD_CHANGED");
        this.userId = userId;
        this.email = email;
        this.changedBy = changedBy;
        this.ipAddress = ipAddress;
    }

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
