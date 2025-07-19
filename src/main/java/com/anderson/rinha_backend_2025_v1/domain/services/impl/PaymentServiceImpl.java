package com.anderson.rinha_backend_2025_v1.domain.services.impl;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.services.IMessageProducer;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentService;
import com.anderson.rinha_backend_2025_v1.infra.db.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository repository;
    private final IMessageProducer messageProducer;
    //private final ExecutorService executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public void save(Payment payment) {
        //executor.submit(() -> repository.save(payment));
        repository.save(payment);
        messageProducer.send(payment);
    }
}
