package net.likelion.bebc25.linkup.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RefundPaymentResponse(
        String mId,
        String paymentKey,
        String orderId,
        String orderName,
        String status,
        String method,
        int totalAmount,
        String requestedAt,
        String approvedAt,

        @JsonProperty("cancels")
        List<Cancels> cancels

) {
    public record Cancels(
            String canceledAt
    ) {
    }

//    public record Cancels(
//            String transactionKey,
//            String cancelReason,
//            int taxExemptionAmount,
//            String canceledAt,
//            int transferDiscountAmount,
//            int easyPayDiscountAmount,
//            String receiptKey,
//            int cancelAmount,
//            int taxFreeAmount,
//            int refundableAmount,
//            String cancelStatus,
//            String cancelRequestId
//    ) {
//    }
}
