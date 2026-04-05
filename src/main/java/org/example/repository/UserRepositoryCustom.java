package org.example.repository;

import org.example.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * Custom repository interface for complex User queries
 * Methods defined here will be implemented in UserRepositoryImpl
 */
public interface UserRepositoryCustom {
    
    /**
     * Find user by phone number or email with custom logic
     * @param identifier phone number or email
     * @return Optional User
     */
    Optional<User> findByPhoneNumberOrEmailCustom(String identifier);
    
    /**
     * Search users by multiple criteria
     * @param name user name (partial match)
     * @param orgName organization name
     * @return List of matching users
     */
    List<User> searchUsers(String name, String orgName);
    
    /**
     * Get users created in a specific time range
     * @param startTime start timestamp in milliseconds
     * @param endTime end timestamp in milliseconds
     * @return List of users
     */
    List<User> findUsersCreatedBetween(Long startTime, Long endTime);
}

