package com.anderson.rinha_backend_2025_v1.domain.services;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;

public interface IMessageProducer {
    void send(Payment payment);
}
