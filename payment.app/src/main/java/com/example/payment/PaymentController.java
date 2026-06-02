// 6th Layer The REST Controller API //
package com.example.payment.controller;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.model.Payment;
import com.example.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentRequest request) {
        Payment processedPayment = paymentService.processPayment(request);
        return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrder(@PathVariable String orderId) {
        Payment payment = paymentService.getPaymentStatus(orderId);
        return ResponseEntity.ok(payment);
    }
}
