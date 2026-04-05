package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Plan plan;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Double feeAmount;

    private String notes;

    @Column(name = "created_at", updatable = false)
    private Long createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
    }
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public enum Plan {
        MONTHLY, QUARTERLY, HALF_YEARLY, YEARLY
    }
}