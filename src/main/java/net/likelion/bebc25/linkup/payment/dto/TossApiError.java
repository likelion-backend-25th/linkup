package net.likelion.bebc25.linkup.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TossApiError(
        String version,
        String traceId,
        String error,
        String code,
        String message
) {
}
