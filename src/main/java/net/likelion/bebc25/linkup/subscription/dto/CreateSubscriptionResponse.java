package net.likelion.bebc25.linkup.subscription.dto;

import net.likelion.bebc25.linkup.payment.domain.Payment;
import net.likelion.bebc25.linkup.subscription.domain.Subscription;

import java.time.LocalDateTime;

public record CreateSubscriptionResponse(
        Long subscriptionId,
        String orderName,
        String status,
        int totalAmount,
        String approvedAt
) {
    public static CreateSubscriptionResponse from(Payment payment) {
        return new CreateSubscriptionResponse(
                payment.getSubscriptionId(),
                payment.getOrderName(),
                payment.getStatus(),
                payment.getTotalAmount(),
                payment.getApprovedAt()
        );
    }
}
