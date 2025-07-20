package com.anderson.rinha_backend_2025_v1.domain.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentSummaryDTO(
        @JsonProperty("default")
        PaymentProcessorDTO defaultPayment,
        PaymentProcessorDTO fallback
) {
}
