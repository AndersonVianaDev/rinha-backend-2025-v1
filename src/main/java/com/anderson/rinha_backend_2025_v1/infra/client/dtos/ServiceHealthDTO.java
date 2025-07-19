package com.anderson.rinha_backend_2025_v1.infra.client.dtos;

public record ServiceHealthDTO(
        boolean failing,
        int minResponseTime
) {
}
