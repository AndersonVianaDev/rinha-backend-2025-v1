package com.anderson.rinha_backend_2025_v1.infra.aws.consumer;

import com.anderson.rinha_backend_2025_v1.domain.model.Payment;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@AllArgsConstructor
public class MessageConsumer implements Runnable {

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ObjectMapper mapper;
    private final IPaymentService service;

    @Override
    public void run() {
        while (true) {
            try {
                ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(10)
                        .build();
                List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
                for (Message message : messages) {
                    Payment payment = mapper.readValue(message.body(), Payment.class);
                    service.process(payment);

                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build());
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
