package org.example.repository;

import org.example.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPhoneNumber(String phoneNumber);
    @Query("SELECT m FROM Member m WHERE m.expiryDate >= :today")
    Page<Member> findActiveMembers(LocalDate today, Pageable pageable);
    @Query("SELECT m FROM Member m WHERE m.expiryDate < :today")
    Page<Member> findExpiredMembers(LocalDate today, Pageable pageable);
    @Query("SELECT COUNT(m) FROM Member m WHERE m.expiryDate >= :today")
    long countActiveMembers(LocalDate today);
    @Query("SELECT COUNT(m) FROM Member m WHERE m.expiryDate < :today")
    long countExpiredMembers(LocalDate today);
}