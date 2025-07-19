package com.anderson.rinha_backend_2025_v1.domain.model;

import com.anderson.rinha_backend_2025_v1.domain.model.enums.ProcessorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    private UUID correlationId;

    private BigDecimal amount;
    private Instant requestedAt;

    @Enumerated(EnumType.STRING)
    private ProcessorType type;
}
