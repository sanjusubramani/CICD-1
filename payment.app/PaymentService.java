//  5th Layer The Business Logic Layer  //
package com.example.payment.service;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        // Industry practice: Here is where you would call a 3rd party API gateway (like Stripe/PayPal)
        // For simplicity, we mock a successful gateway response down below.
        String gatewayStatus = "SUCCESS"; 

        Payment payment = new Payment(
                request.getOrderId(),
                request.getAmount(),
                request.getCurrency(),
                gatewayStatus
        );

        return paymentRepository.save(payment);
    }

    public Payment getPaymentStatus(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for Order ID: " + orderId));
    }
}