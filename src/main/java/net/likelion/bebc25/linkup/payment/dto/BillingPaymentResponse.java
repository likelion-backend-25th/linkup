package net.likelion.bebc25.linkup.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record BillingPaymentResponse(
        String mId,
        String paymentKey,
        String orderId,
        String orderName,
        String status,
        String method,
        int totalAmount,
        String requestedAt,
        String approvedAt

) {
}
