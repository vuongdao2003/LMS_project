package com.example.OnlinePay.repository;

import com.example.OnlinePay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Payment findByTransaction(String transaction);
}
