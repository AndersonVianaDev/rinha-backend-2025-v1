#!/bin/bash

awslocal sqs create-queue --queue-name rinha-dlq

DLQ_ARN=$(awslocal sqs get-queue-attributes --queue-url $(awslocal sqs get-queue-url --queue-name rinha-dlq | jq -r .QueueUrl) --attribute-name QueueArn | jq -r '.Attributes.QueueArn')

awslocal sqs create-queue \
  --queue-name rinha \
  --attributes '{
    "RedrivePolicy": "{\"deadLetterTargetArn\":\"'$DLQ_ARN'\",\"maxReceiveCount\":\"1\"}"
  }' 