package com.anderson.rinha_backend_2025_v1.domain.model.dtos;

import java.math.BigDecimal;

public record PaymentProcessorDTO(
        int totalRequest,
        BigDecimal totalAmount) {
}
