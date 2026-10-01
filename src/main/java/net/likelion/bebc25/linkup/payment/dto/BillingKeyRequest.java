package net.likelion.bebc25.linkup.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record BillingKeyRequest(
        @NotBlank
        String authKey,
        @NotBlank
        String customerKey
) {
}
