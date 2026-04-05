package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    
    // Method 1: Spring Data JPA auto-generates the query based on method name
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    
    // Method 2: Custom JPQL query using @Query annotation
    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber OR u.email = :email")
    Optional<User> findByPhoneNumberOrEmail(@Param("phoneNumber") String phoneNumber, 
                                            @Param("email") String email);
    
    // Method 3: Custom JPQL query - Find users by organization name
    @Query("SELECT u FROM User u JOIN u.organization o WHERE o.orgName = :orgName")
    List<User> findByOrganizationName(@Param("orgName") String orgName);
    
    // Method 4: Native SQL query
    @Query(value = "SELECT * FROM users WHERE phone_number = :phone OR email = :email", 
           nativeQuery = true)
    Optional<User> findByPhoneOrEmailNative(@Param("phone") String phone, 
                                            @Param("email") String email);
    
    // Method 5: Custom query with multiple conditions
    @Query("SELECT u FROM User u WHERE u.organization.id = :orgId AND u.email IS NOT NULL")
    List<User> findUsersWithEmailByOrganization(@Param("orgId") Integer organizationId);
    
    // Method 6: Count query
    @Query("SELECT COUNT(u) FROM User u WHERE u.organization.id = :orgId")
    long countUsersByOrganization(@Param("orgId") Integer organizationId);
}
