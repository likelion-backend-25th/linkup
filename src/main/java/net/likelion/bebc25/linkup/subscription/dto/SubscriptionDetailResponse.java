package net.likelion.bebc25.linkup.subscription.dto;

import java.time.LocalDateTime;

public record SubscriptionDetailResponse(
        Long subscriptionId,
        String name,
        String uniqueId,
        String email,
        int price,
        String status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime nextBillingAt
) {
}
