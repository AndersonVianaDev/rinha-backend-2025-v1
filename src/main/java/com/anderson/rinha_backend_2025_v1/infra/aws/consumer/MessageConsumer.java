package com.anderson.rinha_backend_2025_v1.infra.aws.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageConsumer implements Runnable {
    private final SqsClient sqsClient;

    private final String queueUrl;

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
                    //processMessage(message);
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
