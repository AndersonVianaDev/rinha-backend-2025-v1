package com.anderson.rinha_backend_2025_v1.infra.aws.producer;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.services.IMessageProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducerImpl implements IMessageProducer {

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    private final SqsTemplate sqsTemplate;

    private final ObjectMapper mapper;

    @Override
    public void send(Payment payment) {
        try {
            final String json = mapper.writeValueAsString(payment);
            sqsTemplate.send(queueUrl, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
