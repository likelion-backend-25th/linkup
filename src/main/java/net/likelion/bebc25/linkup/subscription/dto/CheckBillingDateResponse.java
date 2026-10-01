package net.likelion.bebc25.linkup.subscription.dto;

import java.time.LocalDateTime;

public record CheckBillingDateResponse(
        String billingDate
) {
    public static CheckBillingDateResponse from(String billingDate) {
        return new CheckBillingDateResponse(billingDate);
    }
}
