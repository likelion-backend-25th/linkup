package net.likelion.bebc25.linkup.payment.client;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.common.config.TossProperties;
import net.likelion.bebc25.linkup.payment.dto.BillingKeyResponse;
import net.likelion.bebc25.linkup.payment.dto.TossApiError;
import net.likelion.bebc25.linkup.payment.exception.TossApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TossPaymentClient {
    private final RestClient tossRestClient;
    private final TossProperties tossProperties;
    private final ObjectMapper objectMapper;

    private String authorization() {
        String credentials =
                tossProperties.secretKey() + ":";

        String encoded = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        return "Basic " + encoded;
    }

    // 빌링키 발급
    public BillingKeyResponse issueBillingKey(String authKey, String customerKey) {
        try {
            return tossRestClient
                    .post()
                    .uri("/v1/billing/authorizations/issue")
                    .header(HttpHeaders.AUTHORIZATION, authorization())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "authKey", authKey,
                            "customerKey", customerKey
                            )
                    )
                    .retrieve()
                    .body(BillingKeyResponse.class);

        } catch (RestClientResponseException e) {
            throw createTossApiException(e);
        }
    }

     // billingKey를 이용한 자동결제
//    public BillingPaymentResponse charge(
//            String billingKey,
//            String customerKey,
//            String orderId,
//            String orderName,
//            int amount
//    ) {
//
//        try {
//
//            return tossRestClient
//                    .post()
//                    .uri("/v1/billing/{billingKey}", billingKey)
//                    .header(HttpHeaders.AUTHORIZATION, authorization())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(Map.of("customerKey", customerKey,
//                                    "orderId", orderId,
//                                    "orderName", orderName,
//                                    "amount", amount
//                            )
//                    )
//                    .retrieve()
//                    .body(BillingPaymentResponse.class);
//
//        } catch (RestClientResponseException e) {
//            throw createTossApiException(e);
//        }
//    }

    // 빌링키 삭제
    public void deleteBillingKey(String billingKey) {
        try {
            tossRestClient
                    .delete()
                    .uri("/v1/billing/{billingKey}", billingKey)
                    .header(HttpHeaders.AUTHORIZATION, authorization())
                    .retrieve()
                    .toBodilessEntity();

        } catch (org.springframework.web.client.RestClientResponseException e) {
            throw createTossApiException(e);
        }
    }

    private TossApiException createTossApiException(RestClientResponseException e) {
        TossApiError error;

        try {
            error = objectMapper.readValue(e.getResponseBodyAsString(), TossApiError.class);

        } catch (Exception parseException) {
            // 토스가 예상하지 못한 응답을 반환했거나 JSON 파싱에 실패한 경우를 대비한 fallback
            error = new TossApiError(null, null, null,
                    "UNKNOWN_TOSS_ERROR", e.getResponseBodyAsString()
            );
        }

        return new TossApiException(e.getStatusCode().value(), error);
    }

}
