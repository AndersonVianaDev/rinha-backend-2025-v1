package com.anderson.rinha_backend_2025_v1.controller.dtos;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequestDTO(
        UUID correlationId,
        BigDecimal amount
) {
    public static Payment of(PaymentRequestDTO request) {
        return Payment.builder()
                .correlationId(request.correlationId())
                .amount(request.amount())
                .build();
    }
}
