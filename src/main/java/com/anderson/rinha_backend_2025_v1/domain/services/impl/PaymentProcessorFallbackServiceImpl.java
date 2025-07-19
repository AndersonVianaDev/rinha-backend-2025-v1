package com.anderson.rinha_backend_2025_v1.domain.services.impl;

import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentProcessorRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.ServiceHealthDTO;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentProcessorFallbackService;
import com.anderson.rinha_backend_2025_v1.infra.client.PaymentProcessorFallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessorFallbackServiceImpl implements IPaymentProcessorFallbackService {

    private final PaymentProcessorFallback paymentProcessorFallback;

    @Override
    public ServiceHealthDTO serviceHealth() {
        return paymentProcessorFallback.serviceHealth();
    }

    @Override
    public void processPayment(PaymentProcessorRequestDTO request) {
        paymentProcessorFallback.processPayment(request);
    }
}
