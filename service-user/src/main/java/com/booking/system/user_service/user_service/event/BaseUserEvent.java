package com.booking.system.user_service.user_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public abstract class BaseUserEvent {

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("serviceId")
    private String serviceId = "user-service";

    @JsonProperty("version")
    private String version = "1.0";

    public BaseUserEvent(String eventType) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.eventType = eventType;
        this.timestamp = Instant.now().toEpochMilli();
    }

    // Getters and setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}