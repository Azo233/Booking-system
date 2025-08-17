package com.booking.system.user_service.user_service.event;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class UserUpdatedEvent extends BaseUserEvent {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("updatedFields")
    private Map<String, Object> updatedFields;

    @JsonProperty("previousValues")
    private Map<String, Object> previousValues;

    public UserUpdatedEvent() {
        super("USER_UPDATED");
    }

    public UserUpdatedEvent(Long userId, java.util.Map<String, Object> updatedFields,
                            java.util.Map<String, Object> previousValues) {
        super("USER_UPDATED");
        this.userId = userId;
        this.updatedFields = updatedFields;
        this.previousValues = previousValues;
    }

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public java.util.Map<String, Object> getUpdatedFields() { return updatedFields; }
    public void setUpdatedFields(java.util.Map<String, Object> updatedFields) { this.updatedFields = updatedFields; }

    public java.util.Map<String, Object> getPreviousValues() { return previousValues; }
    public void setPreviousValues(java.util.Map<String, Object> previousValues) { this.previousValues = previousValues; }
}

