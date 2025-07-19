package com.anderson.rinha_backend_2025_v1.domain.services.impl;

import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentProcessorRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.ServiceHealthDTO;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentProcessorDefaultService;
import com.anderson.rinha_backend_2025_v1.infra.client.PaymentProcessorDefault;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessorDefaultServiceImpl implements IPaymentProcessorDefaultService {

    private final PaymentProcessorDefault paymentProcessorDefault;


    @Override
    public ServiceHealthDTO serviceHealth() {
        return paymentProcessorDefault.serviceHealth();
    }

    @Override
    public void processPayment(PaymentProcessorRequestDTO request) {
        paymentProcessorDefault.processPayment(request);
    }
}
