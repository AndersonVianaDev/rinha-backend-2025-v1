package com.anderson.rinha_backend_2025_v1.controller;

import com.anderson.rinha_backend_2025_v1.controller.dtos.PaymentRequestDTO;
import com.anderson.rinha_backend_2025_v1.domain.services.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService service;

    @PostMapping("/payments")
    public ResponseEntity<Void> save(@RequestBody PaymentRequestDTO request) {
        service.save(PaymentRequestDTO.of(request));
        return ResponseEntity.ok().build();
    }

}
