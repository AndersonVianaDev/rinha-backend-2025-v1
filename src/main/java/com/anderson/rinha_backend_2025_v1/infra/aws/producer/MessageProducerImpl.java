package com.anderson.rinha_backend_2025_v1.infra.aws.producer;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.services.IMessageProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
public class MessageProducerImpl implements IMessageProducer {

    @Value("${spring.cloud.aws.sqs.queue-url}")
    private String queueUrl;

    @Value("${spring.cloud.aws.sqs.dlq-url}")
    private String dlqUrl;

    private final SqsAsyncClient amazonSQS;

    private final ObjectMapper mapper;

    @Override
    public void send(Payment payment) {
        try {
            final String json = mapper.writeValueAsString(payment);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .build();

            amazonSQS.sendMessage(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendToQueueDlq(Payment payment) {
        try {
            final String json = mapper.writeValueAsString(payment);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(dlqUrl)
                    .messageBody(json)
                    .build();

            amazonSQS.sendMessage(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
