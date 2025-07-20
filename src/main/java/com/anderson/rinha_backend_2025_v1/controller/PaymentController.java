package com.anderson.rinha_backend_2025_v1.controller;

import com.anderson.rinha_backend_2025_v1.controller.dtos.PaymentRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.model.dtos.PaymentSummaryDTO;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService service;

    @PostMapping("/payments")
    public ResponseEntity<Void> save(@RequestBody PaymentRequestDTO request) {
        service.save(PaymentRequestDTO.of(request));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/payments-summary")
    public ResponseEntity<PaymentSummaryDTO> summary(@RequestParam(name = "from", required = false)Instant from,
                                                     @RequestParam(name = "to", required = false) Instant to) {
        final PaymentSummaryDTO summary = service.summary(from, to);
        return ResponseEntity.ok(summary);
    }

}
