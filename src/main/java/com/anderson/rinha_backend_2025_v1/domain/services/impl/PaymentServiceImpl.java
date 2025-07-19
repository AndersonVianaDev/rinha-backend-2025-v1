package com.anderson.rinha_backend_2025_v1.domain.services.impl;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentProcessorRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.enums.ProcessorType;
import com.anderson.rinha_backend_2025_v1.domain.services.*;
import com.anderson.rinha_backend_2025_v1.infra.db.repository.PaymentRepository;
import com.anderson.rinha_backend_2025_v1.infra.exceptions.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository repository;
    private final IMessageProducer messageProducer;
    private final IProcessorCacheService processorCacheService;
    private final IPaymentProcessorDefaultService paymentProcessorDefault;
    private final IPaymentProcessorFallbackService paymentProcessorFallback;
    //private final ExecutorService executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public void save(Payment payment) {
        //executor.submit(() -> repository.save(payment));
        repository.save(payment);
        messageProducer.send(payment);
    }

    @Override
    public void process(Payment payment) {
        final ProcessorType type = ProcessorType.getType(processorCacheService.getCurrentProcessor());
        if (ProcessorType.FAILURE.equals(type)) {
            /*
             * TODO -> avaliar se é necessário persistir o pagamento com status de falha (FAILURE) no banco de dados
             * (NÃO ENTRA NO ESCOPO DO PROJETO)
             */

            // payment.setType(ProcessorType.FAILURE);
            // repository.save(payment);
            throw new UnexpectedException("servers unavailable");
        }

        final Instant now = Instant.now();
        payment.setRequestedAt(now);

        final PaymentProcessorRequestDTO request = new PaymentProcessorRequestDTO(
                payment.getCorrelationId(),
                payment.getAmount(),
                payment.getRequestedAt()
        );

        if (ProcessorType.DEFAULT.equals(type)) {
            try {
                paymentProcessorDefault.processPayment(request);
                payment.setType(ProcessorType.DEFAULT);
            } catch (Exception e) {
                try {
                    paymentProcessorFallback.processPayment(request);
                    payment.setType(ProcessorType.FALLBACK);
                } catch (Exception ex) {
                    throw new UnexpectedException("servers unavailable");
                }
            }
        }

        if (ProcessorType.FALLBACK.equals(type)) {
            try {
                paymentProcessorFallback.processPayment(request);
                payment.setType(ProcessorType.FALLBACK);
            } catch (Exception e) {
                try {
                    paymentProcessorDefault.processPayment(request);
                    payment.setType(ProcessorType.DEFAULT);
                } catch (Exception ex) {
                    throw new UnexpectedException("servers unavailable");
                }
            }
        }

        repository.save(payment);
    }
}
