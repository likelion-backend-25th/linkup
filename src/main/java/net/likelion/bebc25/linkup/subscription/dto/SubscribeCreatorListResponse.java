package net.likelion.bebc25.linkup.subscription.dto;

import java.time.LocalDateTime;

public record SubscribeCreatorListResponse(
        Long subscriptionId,
        Long memberId,
        Long creatorId,
        String creatorName,
        String creatorUniqueId,
        String profileImage,
        String introduction,
        String status
) {
}
