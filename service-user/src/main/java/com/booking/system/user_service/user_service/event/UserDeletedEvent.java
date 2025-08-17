package com.booking.system.user_service.user_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDeletedEvent extends BaseUserEvent {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("deletedBy")
    private Long deletedBy;

    @JsonProperty("reason")
    private String reason;

    public UserDeletedEvent() {
        super("USER_DELETED");
    }

    public UserDeletedEvent(Long userId, String email, Long deletedBy, String reason) {
        super("USER_DELETED");
        this.userId = userId;
        this.email = email;
        this.deletedBy = deletedBy;
        this.reason = reason;
    }

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getDeletedBy() { return deletedBy; }
    public void setDeletedBy(Long deletedBy) { this.deletedBy = deletedBy; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
