package net.likelion.bebc25.linkup.subscription.dto;

import java.time.LocalDateTime;

public record SubscribeCreatorListResponse(
        Long subscription_id,
        Long memberId,
        Long creatorId,
        String creatorName,
        String creatorUniqueId,
        String profileImage,
        String introduction,
        String status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime nextBillingAt
) {
}
