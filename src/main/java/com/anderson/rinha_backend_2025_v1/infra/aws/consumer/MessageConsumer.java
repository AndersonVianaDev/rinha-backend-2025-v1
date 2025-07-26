package com.anderson.rinha_backend_2025_v1.infra.aws.consumer;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.services.IMessageProducer;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final ObjectMapper mapper;
    private final IPaymentService service;
    private final IMessageProducer messageProducer;

    @SqsListener("${spring.cloud.aws.sqs.queue-url}")
    public void consumer(List<Payment> payments) {
        System.out.println("Thread: " + Thread.currentThread().getName() + " | Batch size: " + payments.size());
        payments.parallelStream().forEach(payment -> {
            try {
                service.process(payment);
            } catch (Exception e) {
                System.out.println("Error processing payment: " + e.getMessage());
                messageProducer.sendToQueueDlq(payment);
            }
        });
    }
}
