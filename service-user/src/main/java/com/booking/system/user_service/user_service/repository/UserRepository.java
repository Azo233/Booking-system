package com.booking.system.user_service.user_service.repository;

import com.booking.system.user_service.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail (String email);

}
