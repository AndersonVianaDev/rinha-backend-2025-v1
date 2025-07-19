package com.anderson.rinha_backend_2025_v1.domain.services;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;

public interface IPaymentService {
    void save(Payment payment);
    void process(Payment payment);
}
