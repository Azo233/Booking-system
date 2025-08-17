package com.booking.system.user_service.user_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationEvent {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("message")
    private String message;

    @JsonProperty("notificationType")
    private String notificationType; // EMAIL, SMS, PUSH

    @JsonProperty("timestamp")
    private long timestamp;

    public NotificationEvent() {}

    public NotificationEvent(String userId, String eventType, String message,
                             String notificationType, long timestamp) {
        this.userId = userId;
        this.eventType = eventType;
        this.message = message;
        this.notificationType = notificationType;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}