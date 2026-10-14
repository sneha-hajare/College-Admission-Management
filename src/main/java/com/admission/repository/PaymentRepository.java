package com.admission.repository;

import com.admission.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByApplicationId(Long applicationId);

    List<Payment> findByStatus(String status);

    Optional<Payment> findByTransactionId(String transactionId);
}