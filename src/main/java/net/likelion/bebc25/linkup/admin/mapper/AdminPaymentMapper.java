package net.likelion.bebc25.linkup.admin.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminPaymentDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminPaymentMapper {

    // 관리자 결제 목록 조회
    List<AdminPaymentResponse> findPayments(AdminPaymentSearchRequest request);

    // 관리자 결제 전체 개수 조회
    long countPayments(AdminPaymentSearchRequest request);

    // 관리자 결제 상세 조회
    AdminPaymentDetailResponse findPaymentDetail(Long paymentId);
}