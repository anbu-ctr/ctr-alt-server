package org.example.dto;

import lombok.Data;
import org.example.entity.Payment.PaymentMode;
import org.example.entity.Payment.PaymentStatus;
import java.time.LocalDate;

@Data
public class PaymentDto {
    private Long id;
    private Long memberId;
    private Double amount;
    private PaymentMode paymentMode;
    private LocalDate paymentDate;
    private String notes;
    private PaymentStatus paymentStatus;
    private Long createdAt;
}

