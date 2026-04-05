package org.example.dto;

import lombok.Data;
import org.example.entity.Payment.PaymentMode;
import java.time.LocalDate;

@Data
public class PaymentRequest {
    private String type;
    private PaymentData data;

    @Data
    public static class PaymentData {
        private Long memberId;
        private Double amount;
        private PaymentMode paymentMode;
        private LocalDate paymentDate;
        private String notes;
    }
}

