package net.likelion.bebc25.linkup.admin.service;

import net.likelion.bebc25.linkup.admin.dto.AdminPaymentDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentSearchRequest;
import net.likelion.bebc25.linkup.admin.mapper.AdminPaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final AdminPaymentMapper adminPaymentMapper;

    public AdminPaymentServiceImpl(AdminPaymentMapper adminPaymentMapper){
        this.adminPaymentMapper = adminPaymentMapper;
    }

    @Override
    public List<AdminPaymentResponse> findPayments(AdminPaymentSearchRequest condition) {
        return adminPaymentMapper.findPayments(condition);
    }

    @Override
    public long countPayments(AdminPaymentSearchRequest condition) {
        return adminPaymentMapper.countPayments(condition);
    }

    @Override
    public AdminPaymentDetailResponse findPaymentDetail(Long paymentId) {
        return adminPaymentMapper.findPaymentDetail(paymentId);
    }
}
