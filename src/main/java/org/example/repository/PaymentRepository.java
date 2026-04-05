package org.example.repository;

import org.example.entity.Member;
import org.example.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByMember(Member member, Pageable pageable);

    List<Payment> findByMemberAndPaymentDate(Member member, LocalDate paymentDate);

    Page<Payment> findAll(Pageable pageable);

    Optional<Payment> findById(Long paymentId);

    List<Payment> findByMember(Member member);

    List<Payment> findByPaymentDate(LocalDate paymentDate);
}

