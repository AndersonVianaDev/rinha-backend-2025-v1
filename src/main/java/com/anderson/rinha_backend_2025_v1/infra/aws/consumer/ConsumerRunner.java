package com.anderson.rinha_backend_2025_v1.infra.aws.consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@RequiredArgsConstructor
public class ConsumerRunner {
    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Value("${aws.sqs.consumer-count:10}")
    private int consumerCount;

    @PostConstruct
    public void startConsumers() {
        for (int i = 1; i <= consumerCount; i++) {
            Thread.startVirtualThread(() -> {
                new MessageConsumer(sqsClient, queueUrl).run();
            });
        }
    }
}