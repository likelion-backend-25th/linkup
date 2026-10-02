package net.likelion.bebc25.linkup.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "toss.payments")
public record TossProperties(
        String secretKey,
        String apiUrl
) {
}
