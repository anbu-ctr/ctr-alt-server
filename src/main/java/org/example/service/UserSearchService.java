package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Example service showing how to use custom repository methods
 */
@Service
public class UserSearchService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Example 1: Using @Query annotation method
     * Find user by phone number or email in a single query
     */
    public Optional<User> findUserByPhoneOrEmail(String phoneNumber, String email) {
        return userRepository.findByPhoneNumberOrEmail(phoneNumber, email);
    }

    /**
     * Example 2: Using custom implementation method
     * Find user by identifier (could be phone or email)
     */
    public Optional<User> findUserByIdentifier(String identifier) {
        return userRepository.findByPhoneNumberOrEmailCustom(identifier);
    }

    /**
     * Example 3: Using method name convention (Spring Data JPA auto-implements)
     * Find user by exact phone number
     */
    public Optional<User> findByPhone(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    /**
     * Example 4: Using custom search with multiple criteria
     * Search users by name and organization
     */
    public List<User> searchUsersByNameAndOrg(String name, String orgName) {
        return userRepository.searchUsers(name, orgName);
    }

    /**
     * Example 5: Using custom method with native query
     * Find users by phone or email using native SQL
     */
    public Optional<User> findByPhoneOrEmailNative(String phone, String email) {
        return userRepository.findByPhoneOrEmailNative(phone, email);
    }

    /**
     * Example 6: Find users by organization name
     */
    public List<User> getUsersByOrganization(String orgName) {
        return userRepository.findByOrganizationName(orgName);
    }

    /**
     * Example 7: Find users created in a time range
     */
    public List<User> getUsersCreatedBetween(Long startTime, Long endTime) {
        return userRepository.findUsersCreatedBetween(startTime, endTime);
    }

    /**
     * Example 8: Get users with email by organization
     */
    public List<User> getUsersWithEmailByOrganization(Integer orgId) {
        return userRepository.findUsersWithEmailByOrganization(orgId);
    }

    /**
     * Example 9: Count users in an organization
     */
    public long countUsersInOrganization(Integer orgId) {
        return userRepository.countUsersByOrganization(orgId);
    }

    /**
     * Example 10: Using built-in JPA methods
     * Find all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Example 11: Find user by ID
     */
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Example 12: Delete user
     */
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}

