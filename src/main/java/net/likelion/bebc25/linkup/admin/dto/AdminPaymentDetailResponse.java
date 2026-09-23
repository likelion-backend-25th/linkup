package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDateTime;

public record AdminPaymentDetailResponse(

        // 결제 고유 번호
        Long paymentId,

        // 결제 일시
        LocalDateTime paymentDate,

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

        // 결제 고유 거래 번호
        String merchantUid,

        // PG사 결제 번호
        String impUid,

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