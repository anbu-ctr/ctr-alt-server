package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.entity.Organization;
import org.example.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of custom repository methods
 * Class name MUST be: [RepositoryName]Impl
 */
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findByPhoneNumberOrEmailCustom(String identifier) {
        // Using JPQL
        String jpql = "SELECT u FROM User u WHERE u.phoneNumber = :identifier OR u.email = :identifier";
        
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("identifier", identifier);
        
        List<User> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<User> searchUsers(String name, String orgName) {
        // Using Criteria API for dynamic queries
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        Join<User, Organization> organization = user.join("organization");
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Add conditions dynamically
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(user.get("name")), "%" + name.toLowerCase() + "%"));
        }
        
        if (orgName != null && !orgName.isEmpty()) {
            predicates.add(cb.equal(organization.get("orgName"), orgName));
        }
        
        // Combine all predicates with AND
        cq.where(predicates.toArray(new Predicate[0]));
        
        TypedQuery<User> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<User> findUsersCreatedBetween(Long startTime, Long endTime) {
        // Using native SQL query for complex operations
        String sql = "SELECT * FROM users WHERE created_at BETWEEN ?1 AND ?2 ORDER BY created_at DESC";
        
        @SuppressWarnings("unchecked")
        List<User> results = entityManager.createNativeQuery(sql, User.class)
                .setParameter(1, startTime)
                .setParameter(2, endTime)
                .getResultList();
        
        return results;
    }
}

