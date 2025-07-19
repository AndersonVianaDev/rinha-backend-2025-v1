package com.anderson.rinha_backend_2025_v1.domain.services;

import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentProcessorRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.ServiceHealthDTO;

public interface IPaymentProcessorFallbackService {
    ServiceHealthDTO serviceHealth();
    void processPayment(PaymentProcessorRequestDTO request);
}
