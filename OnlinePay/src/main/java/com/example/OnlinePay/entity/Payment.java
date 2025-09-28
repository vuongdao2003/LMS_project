package com.example.OnlinePay.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String orderId;

    long amount;
    @Enumerated(EnumType.STRING)
    PaymentStatus status;
    String transaction;
     String paymentMethod;

    LocalDateTime createdAt;

     LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED,
        REFUNDED
    }
    String createdDate;
}
