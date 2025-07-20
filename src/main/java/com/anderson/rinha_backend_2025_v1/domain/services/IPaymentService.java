package com.anderson.rinha_backend_2025_v1.domain.services;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentSummaryDTO;

import java.time.Instant;

public interface IPaymentService {
    void save(Payment payment);
    void process(Payment payment);
    PaymentSummaryDTO summary(Instant startDate, Instant endDate);
}
