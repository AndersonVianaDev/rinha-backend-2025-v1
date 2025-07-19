package com.anderson.rinha_backend_2025_v1.domain.model.dtos;

public record ServiceHealthDTO(
        boolean failing,
        int minResponseTime
) {
}
