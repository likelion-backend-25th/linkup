package net.likelion.bebc25.linkup.subscription.dto;

import net.likelion.bebc25.linkup.subscription.domain.Subscription;

public record CreateSubscriptionResponse(
        Long subscriptionId
) {
    public static CreateSubscriptionResponse from(Subscription subscription) {
        return new CreateSubscriptionResponse(subscription.getSubscriptionId());
    }
}
