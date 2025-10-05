package com.example.onlinepay.repository;

import com.example.onlinepay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Payment findByTransaction(String transaction);
    Payment findByOrderId(String orderId);
}
