package net.likelion.bebc25.linkup.subscription.dto;

import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id,
        Long creatorId,
        Long memberId,
        String customerUid,
        int price,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String status,
        LocalDateTime nextBillingAt
) {
}
