package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,
    cascade = {CascadeType.ALL})
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_payment_member"))
    private Member member;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PaymentMode paymentMode;

    @Column(nullable = false)
    private LocalDate paymentDate;

    private String notes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", updatable = false)
    private Long createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.COMPLETED;
        }
    }

    public enum PaymentMode {
        CASH, CARD, UPI, CHEQUE, ONLINE_TRANSFER
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
}

