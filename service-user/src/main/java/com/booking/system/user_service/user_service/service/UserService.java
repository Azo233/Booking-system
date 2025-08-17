package com.booking.system.user_service.user_service.service;

import com.booking.system.user_service.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KafkaEventService kafkaEventService;

    public UserService(UserRepository userRepository, KafkaEventService kafkaEventService) {
        this.userRepository = userRepository;
        this.kafkaEventService = kafkaEventService;
    }

    
}
