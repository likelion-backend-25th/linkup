package net.likelion.bebc25.linkup.subscription.dto;

public record CreateSubscriptionRequest(
        Long creatorId,
        String customerUid,
        int price
) { }
