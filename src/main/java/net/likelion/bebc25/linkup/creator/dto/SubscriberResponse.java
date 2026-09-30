package net.likelion.bebc25.linkup.creator.dto;

import java.time.LocalDateTime;

public record SubscriberResponse(
        Long subscriptionId,
        Long creatorId,
        Long memberId,
        String memberName,
        String memberUniqueId,
        String profileImage,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime nextBillingAt
) {
}
