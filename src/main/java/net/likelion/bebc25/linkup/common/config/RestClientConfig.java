package net.likelion.bebc25.linkup.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient tossRestClient(
            TossProperties properties
    ) {
        return RestClient.builder()
                .baseUrl(properties.apiUrl())
                .build();
    }
}
