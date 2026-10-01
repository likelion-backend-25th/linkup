package net.likelion.bebc25.linkup.subscription.dto;

public record RefundSubscriptionResponse(
        int totalAmount,
        String canceledAt
) {
    public static RefundSubscriptionResponse from(int totalAmount, String canceledAt) {
        return new RefundSubscriptionResponse(totalAmount, canceledAt);
    }
}
