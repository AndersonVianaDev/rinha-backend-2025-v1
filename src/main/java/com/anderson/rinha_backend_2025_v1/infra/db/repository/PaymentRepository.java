package com.anderson.rinha_backend_2025_v1.infra.db.repository;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.model.enums.ProcessorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("select p from Payment p " +
            "where p.requestedAt >= :startDate " +
            "and p.requestedAt <= :endDate " +
            "and p.type = :type")
    List<Payment> findByTypeAndRequestedAtBetween(
            @Param("type") ProcessorType type,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

    List<Payment> findByType(ProcessorType type);
}
