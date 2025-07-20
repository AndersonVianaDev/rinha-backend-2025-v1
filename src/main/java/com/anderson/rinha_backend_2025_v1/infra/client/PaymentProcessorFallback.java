package com.anderson.rinha_backend_2025_v1.infra.client;

import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentProcessorRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.ServiceHealthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PaymentProcessorFallback", url = "${api.payment.url.fallback}")
public interface PaymentProcessorFallback {

    @GetMapping("/service-health")
    ServiceHealthDTO serviceHealth();

    @PostMapping(consumes = "application/json")
    void processPayment(@RequestBody PaymentProcessorRequestDTO request);
}
