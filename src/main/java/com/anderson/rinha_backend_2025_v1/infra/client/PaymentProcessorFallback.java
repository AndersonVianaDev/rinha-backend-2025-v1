package com.anderson.rinha_backend_2025_v1.infra.client;

import com.anderson.rinha_backend_2025_v1.infra.client.dtos.ServiceHealthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "PaymentProcessorFallback", url = "${api.payment.fallback.url}")
public interface PaymentProcessorFallback {

    @GetMapping("/service-health")
    ServiceHealthDTO serviceHealth();
}
