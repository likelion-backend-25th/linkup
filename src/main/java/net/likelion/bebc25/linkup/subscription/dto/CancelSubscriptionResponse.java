package net.likelion.bebc25.linkup.subscription.dto;

import java.time.LocalDateTime;

public record CancelSubscriptionResponse(
        LocalDateTime endDate
) {
    public static CancelSubscriptionResponse from(LocalDateTime endDate) {
        return new CancelSubscriptionResponse(endDate);
    }
}
