package com.anderson.rinha_backend_2025_v1.domain.services.impl;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentProcessorDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentProcessorRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentSummaryDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.enums.ProcessorType;
import com.anderson.rinha_backend_2025_v1.domain.services.*;
import com.anderson.rinha_backend_2025_v1.infra.db.repository.PaymentRepository;
import com.anderson.rinha_backend_2025_v1.infra.exceptions.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository repository;
    private final IMessageProducer messageProducer;
    private final IProcessorCacheService processorCacheService;
    private final IPaymentProcessorDefaultService paymentProcessorDefault;
    private final IPaymentProcessorFallbackService paymentProcessorFallback;

    @Override
    public void save(Payment payment) {
        messageProducer.send(payment);
    }

    @Override
    public void process(Payment payment) {
        final ProcessorType type = ProcessorType.getType(processorCacheService.getCurrentProcessor());
        if (isNull(type) || ProcessorType.FAILURE.equals(type)) {
            throw new UnexpectedException("Unable to determine a valid payment processor");
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
                    throw new UnexpectedException("Payment processing failed using both DEFAULT and FALLBACK processors");
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
                    throw new UnexpectedException("Payment processing failed using both FALLBACK and DEFAULT processors.");
                }
            }
        }

        repository.save(payment);
    }

    @Override
    public PaymentSummaryDTO summary(Instant startDate, Instant endDate) {
        List<Payment> paymentsDefault = new ArrayList<>();
        List<Payment> paymentsFallback = new ArrayList<>();

        if(isNull(startDate) && isNull(endDate)) {
            paymentsDefault =  repository.findByType(ProcessorType.DEFAULT);
            paymentsFallback =  repository.findByType(ProcessorType.FALLBACK);
        }

        if(nonNull(startDate) && nonNull(endDate)) {
            paymentsDefault =  repository.findByTypeAndRequestedAtBetween(ProcessorType.DEFAULT, startDate, endDate);
            paymentsFallback =  repository.findByTypeAndRequestedAtBetween(ProcessorType.FALLBACK, startDate, endDate);
        }

        final BigDecimal defaultAmountTotal = paymentsDefault.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal fallbackAmountTotal = paymentsFallback.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final int defaultRequestTotal = paymentsDefault.size();
        final int fallbackRequestTotal = paymentsFallback.size();

        final PaymentProcessorDTO defaults = new PaymentProcessorDTO(defaultRequestTotal, defaultAmountTotal);
        final PaymentProcessorDTO fallbacks = new PaymentProcessorDTO(fallbackRequestTotal, fallbackAmountTotal);

        return new PaymentSummaryDTO(defaults, fallbacks);
    }
}
