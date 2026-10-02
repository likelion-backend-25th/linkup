package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDateTime;

public record AdminPaymentDetailResponse(

        // 결제 고유 번호
        Long paymentId,

        // 결제 일시
        String paymentDate,

        // 구매자 닉네임
        String buyerNickname,

        // 구매자 아이디
        String buyerId,

        // 결제 금액
        int amount,

        // 결제 상태
        String paymentStatus,

        // 결제 수단
        String paymentMethod,

        // 주문 번호
        String orderId,

        // 토스페이먼츠 결제 키
        String paymentKey,

        // 판매자 닉네임
        String sellerNickname,

        // 판매자 아이디
        String sellerId,

        // 구독 시작일
        LocalDateTime subStartDate,

        // 구독 종료일
        LocalDateTime subEndDate,

        // 다음 결제일
        LocalDateTime nextBillingAt,

        // 구독 상태
        String subStatus
) {

}