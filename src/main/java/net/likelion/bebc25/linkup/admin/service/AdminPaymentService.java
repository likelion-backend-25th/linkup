package net.likelion.bebc25.linkup.admin.service;

import net.likelion.bebc25.linkup.admin.dto.AdminPaymentDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentSearchRequest;

import java.util.List;

public interface AdminPaymentService {

    // 관리자 구독, 결제 목록 조회
    List<AdminPaymentResponse> findPayments(AdminPaymentSearchRequest condition);

    // 관리자 구독, 결제 전체 개수 조회
    long countPayments(AdminPaymentSearchRequest condition);

    // 관리자 결제 상세 조회
    AdminPaymentDetailResponse findPaymentDetail(Long paymentId);
}
