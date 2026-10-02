package net.likelion.bebc25.linkup.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BillingKeyResponse(
        String mId,
        String customerKey,
        String billingKey,
        String authenticatedAt,
        String method,

        @JsonProperty("card")
        Card card
) {
    public record Card(
            String issuerCode,
            String number,
            String cardType,
            String ownerType
    ) {
    }
}
