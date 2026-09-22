package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDateTime;

public record AdminPaymentResponse(

        // 결제 고유 ID
        Long paymentId,

        // 결제 일시
        LocalDateTime paymentDate,

        // 판매자명
        String sellerNickname,

        // 구매자명
        String buyerNickname,

        // 결제 금액
        int amount,

        // 결제 상태
        String paymentStatus
) {


}
