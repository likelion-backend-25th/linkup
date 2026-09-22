package net.likelion.bebc25.linkup.admin.controller;

import net.likelion.bebc25.linkup.admin.dto.AdminPaymentDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentSearchRequest;
import net.likelion.bebc25.linkup.admin.service.AdminPaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/payment")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    public AdminPaymentController(AdminPaymentService adminPaymentService){
        this.adminPaymentService = adminPaymentService;
    }

    @GetMapping
    public List<AdminPaymentResponse> findPayments(AdminPaymentSearchRequest request){
        return adminPaymentService.findPayments(request);
    }

    // 관리자 결제 상세 조회
    @GetMapping("/{paymentId}")
    public AdminPaymentDetailResponse findPaymentDetail(
            @PathVariable Long paymentId
    ) {
        return adminPaymentService.findPaymentDetail(paymentId);
    }
}
