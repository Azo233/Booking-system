package com.booking.system.user_service.user_service.service;

import com.booking.system.user_service.user_service.event.*;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaEventService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventService.class);

    public static final String USER_CREATED_TOPIC = "user.created";
    public static final String USER_UPDATED_TOPIC = "user.updated";
    public static final String USER_DELETED_TOPIC = "user.deleted";
    public static final String USER_PASSWORD_CHANGED_TOPIC = "user.password.changed";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    public void publishUserCreatedEvent(UserCreatedEvent event) {
        publishEvent(USER_CREATED_TOPIC, event.getUserId().toString(), event);
        logger.info("Published UserCreatedEvent for user: {}", event.getUserId());
    }

    public void publishUserUpdatedEvent(UserUpdatedEvent event) {
        publishEvent(USER_UPDATED_TOPIC, event.getUserId().toString(), event);
        logger.info("Published UserUpdatedEvent for user: {}", event.getUserId());
    }


    public void publishUserDeletedEvent(UserDeletedEvent event) {
        publishEvent(USER_DELETED_TOPIC, event.getUserId().toString(), event);
        logger.info("Published UserDeletedEvent for user: {}", event.getUserId());
    }


    public void publishPasswordChangedEvent(UserPasswordChangedEvent event) {
        publishEvent(USER_PASSWORD_CHANGED_TOPIC, event.getUserId().toString(), event);
        logger.info("Published UserPasswordChangedEvent for user: {}", event.getUserId());
    }

    private void publishEvent(String topic, String key, Object event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);

            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(topic, key, eventJson);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to publish event to topic {}: {}", topic, ex.getMessage());
                } else {
                    logger.debug("Event published successfully to topic {} with key {}", topic, key);
                }
            });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event for topic {}: {}", topic, e.getMessage());
        }
    }

    public void publishNotificationEvent(String userId, String eventType, String message) {
        NotificationEvent notificationEvent = new NotificationEvent(
                userId,
                eventType,
                message,
                "EMAIL", // notification type
                System.currentTimeMillis()
        );

        publishEvent("notification.requested", userId, notificationEvent);
        logger.info("Published notification event for user: {}", userId);
    }
}
