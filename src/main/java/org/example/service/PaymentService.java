package org.example.service;

import org.example.dto.PaymentRequest;
import org.example.dto.PaymentDto;
import org.example.entity.Payment;
import org.example.entity.Member;
import org.example.repository.PaymentRepository;
import org.example.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Creates a new payment record for a member
     */
    public Payment createPayment(final PaymentRequest request) {
        final PaymentRequest.PaymentData data = request.getData();

        final Optional<Member> memberOpt = memberRepository.findById(data.getMemberId());
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("Member with ID " + data.getMemberId() + " not found");
        }

        // Validate amount
        if (data.getAmount() == null || data.getAmount() <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }

        final Member member = memberOpt.get();
        final Payment payment = new Payment();
        payment.setMember(member);
        payment.setAmount(data.getAmount());
        payment.setPaymentMode(data.getPaymentMode());
        payment.setPaymentDate(data.getPaymentDate());
        payment.setNotes(data.getNotes());
        payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);

        return paymentRepository.save(payment);
    }

    /**
     * Retrieves payments for a specific member with pagination
     */
    public List<PaymentDto> getPaymentsByMember(final Long memberId, final int start, final int limit) {
        // Validate member exists
        final Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("Member with ID " + memberId + " not found");
        }

        final Member member = memberOpt.get();
        final Pageable pageable = PageRequest.of(start, limit, Sort.by(Sort.Direction.DESC, "paymentDate"));
        final Page<Payment> payments = paymentRepository.findByMember(member, pageable);

        return payments.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all payments with pagination
     */
    public List<PaymentDto> getAllPayments(final int start, final int limit) {
        final Pageable pageable = PageRequest.of(start, limit, Sort.by(Sort.Direction.DESC, "paymentDate"));
        final Page<Payment> payments = paymentRepository.findAll(pageable);

        return payments.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific payment by ID
     */
    public PaymentDto getPaymentById(final Long paymentId) {
        final Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new RuntimeException("Payment with ID " + paymentId + " not found");
        }

        return convertToDto(payment.get());
    }

    /**
     * Updates an existing payment
     */
    public Payment updatePayment(final Long paymentId, final PaymentRequest request) {
        final Optional<Payment> existingPayment = paymentRepository.findById(paymentId);
        if (!existingPayment.isPresent()) {
            throw new RuntimeException("Payment with ID " + paymentId + " not found");
        }

        final PaymentRequest.PaymentData data = request.getData();
        final Payment payment = existingPayment.get();

        if (data.getAmount() != null && data.getAmount() > 0) {
            payment.setAmount(data.getAmount());
        }
        if (data.getPaymentMode() != null) {
            payment.setPaymentMode(data.getPaymentMode());
        }
        if (data.getPaymentDate() != null) {
            payment.setPaymentDate(data.getPaymentDate());
        }
        if (data.getNotes() != null) {
            payment.setNotes(data.getNotes());
        }

        return paymentRepository.save(payment);
    }

    /**
     * Deletes a payment record
     */
    public void deletePayment(final Long paymentId) {
        final Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new RuntimeException("Payment with ID " + paymentId + " not found");
        }

        paymentRepository.deleteById(paymentId);
    }

    /**
     * Gets total payments for a member
     */
    public Double getTotalPaymentsByMember(final Long memberId) {
        final Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("Member with ID " + memberId + " not found");
        }
        
        final Member member = memberOpt.get();
        final List<Payment> payments = paymentRepository.findByMember(member);
        return payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Converts Payment entity to PaymentDto
     */
    private PaymentDto convertToDto(final Payment payment) {
        final PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setMemberId(payment.getMember().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMode(payment.getPaymentMode());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setNotes(payment.getNotes());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setCreatedAt(payment.getCreatedAt());

        return dto;
    }
}

