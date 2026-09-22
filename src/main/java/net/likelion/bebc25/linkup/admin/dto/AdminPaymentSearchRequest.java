package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDate;

public record AdminPaymentSearchRequest(
        // 검색 및 필터 조건 정의
        // 닉네임 or 아이디
        String keyword,

        // 결제 상태
        String paymentStatus,

        // 검색 시작일
        LocalDate startDate,

        // 검색 종료일
        LocalDate endDate,

        // 현재 페이지
        int page,

        // 한 페이지당 건 수
        int size
) {
    public int offset(){
        return (page -1) * size;
    }
}
