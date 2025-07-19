package com.anderson.rinha_backend_2025_v1.domain.model.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentProcessorRequestDTO(
        UUID correlationId,
        BigDecimal amount,
        Instant requestedAt
) {
}
