package net.likelion.bebc25.linkup.payment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.likelion.bebc25.linkup.payment.dto.BillingPaymentResponse;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class Payment {
    private Long paymentId;
    private Long memberId;
    private Long subscriptionId;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String status;
    private String method;
    private int totalAmount;
    private String requestedAt;
    private String approvedAt;
}
