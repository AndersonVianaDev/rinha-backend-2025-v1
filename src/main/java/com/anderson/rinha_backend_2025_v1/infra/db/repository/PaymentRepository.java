package com.anderson.rinha_backend_2025_v1.infra.db.repository;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
