package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", updatable = false)
    private Long createdAt;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_organization"))
    private Organization organization;

    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
    }
}