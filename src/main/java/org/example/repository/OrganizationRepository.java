package org.example.repository;

import org.example.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findByOrgName(String orgName);
    boolean existsByOrgName(String orgName);
}

