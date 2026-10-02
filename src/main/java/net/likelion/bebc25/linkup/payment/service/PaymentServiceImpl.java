package net.likelion.bebc25.linkup.payment.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.payment.client.TossPaymentClient;
import net.likelion.bebc25.linkup.payment.dto.BillingKeyRequest;
import net.likelion.bebc25.linkup.payment.dto.BillingKeyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{
    private final TossPaymentClient tossPaymentClient;
}
