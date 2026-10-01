package net.likelion.bebc25.linkup.subscription.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.payment.client.TossPaymentClient;
import net.likelion.bebc25.linkup.payment.domain.Payment;
import net.likelion.bebc25.linkup.payment.dto.BillingKeyResponse;
import net.likelion.bebc25.linkup.payment.dto.BillingPaymentResponse;
import net.likelion.bebc25.linkup.payment.dto.RefundPaymentResponse;
import net.likelion.bebc25.linkup.payment.mapper.PaymentMapper;
import net.likelion.bebc25.linkup.subscription.domain.Subscription;
import net.likelion.bebc25.linkup.subscription.dto.*;
import net.likelion.bebc25.linkup.subscription.mapper.SubscriptionMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionMapper subscriptionMapper;
    private final TossPaymentClient tossPaymentClient;
    private final PaymentMapper paymentMapper;

    @Override
    @PreAuthorize("@subscriptionServiceImpl.isAuthor(#subscriptionId, authentication.principal.id)")
    public CancelSubscriptionResponse cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionMapper.findBySubscriptionId(subscriptionId);
        tossPaymentClient.deleteBillingKey(subscription.getBillingKey());
        subscriptionMapper.updateCancel(subscriptionId);
        return CancelSubscriptionResponse.from(subscription.getEndDate());
    }

    @Override
    public RefundSubscriptionResponse refundSubscription(Long memberId, Long subscriptionId) {
        Payment payment = paymentMapper.findBySubscriptionId(subscriptionId);

        RefundPaymentResponse refundResponse = tossPaymentClient.refund(payment.getPaymentKey());
        Payment refundPayment = Payment.builder()
                .memberId(memberId)
                .subscriptionId(subscriptionId)
                .paymentKey(refundResponse.paymentKey())
                .orderId(refundResponse.orderId())
                .orderName(refundResponse.orderName())
                .status(refundResponse.status())
                .method(refundResponse.method())
                .totalAmount(refundResponse.totalAmount())
                .requestedAt(refundResponse.requestedAt())
                .approvedAt(refundResponse.approvedAt())
                .canceledAt(refundResponse.cancels().getFirst().canceledAt())
                .build();

        paymentMapper.save(refundPayment);
        subscriptionMapper.updateRemoved(subscriptionId);

        return RefundSubscriptionResponse.from(
                refundResponse.totalAmount(), refundResponse.cancels().getFirst().canceledAt()
        );
    }

    @Override
    @Scheduled(fixedDelay = 60_000) // 1분 간격
    public int deleteExpiredSubscriptions() {
        return subscriptionMapper.deleteExpirations();
    }

    @Override
    @Scheduled(fixedDelay = 60_000) // 1분 간격
    public int autoPayment() {
        List<Subscription> renewalList = subscriptionMapper.findAutoPaymentRenewalList();

        if (renewalList != null) {
            for (Subscription subscription: renewalList) {
                String orderId = "Order_" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 20);

                BillingPaymentResponse paymentResponse =  tossPaymentClient.charge(
                        subscription.getBillingKey(),
                        subscription.getCustomerKey(),
                        orderId,
                        "구독 1개월",
                        subscription.getPrice()
                );

                Payment payment = Payment.builder()
                        .memberId(subscription.getMemberId())
                        .subscriptionId(subscription.getSubscriptionId())
                        .paymentKey(paymentResponse.paymentKey())
                        .orderId(paymentResponse.orderId())
                        .orderName(paymentResponse.orderName())
                        .status(paymentResponse.status())
                        .method(paymentResponse.method())
                        .totalAmount(paymentResponse.totalAmount())
                        .requestedAt(paymentResponse.requestedAt())
                        .approvedAt(paymentResponse.approvedAt())
                        .canceledAt(null)
                        .build();

                paymentMapper.save(payment);

                subscriptionMapper.updateNextBillingAt(subscription.getSubscriptionId());
            }
        }
        return 0;
    }

    @Override
    public CheckBillingDateResponse checkBillingDate(Long subscriptionId) {
        return CheckBillingDateResponse.from(
                paymentMapper.findBillingDate(subscriptionId)
        );
    }


    @Override
    public CreateSubscriptionResponse createSubscription(
            Long memberId, CreateSubscriptionRequest createSubscriptionRequest
    ) {
        BillingKeyResponse response = tossPaymentClient.issueBillingKey(
                createSubscriptionRequest.authKey(), createSubscriptionRequest.customerKey()
        );

        Subscription subscription = Subscription.builder()
                .memberId(memberId)
                .creatorId(createSubscriptionRequest.creatorId())
                .customerKey(createSubscriptionRequest.customerKey())
                .billingKey(response.billingKey())
                .price(4900)
                .build();
        subscriptionMapper.save(subscription);

        String orderId = "Order_" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 20);

        BillingPaymentResponse paymentResponse =  tossPaymentClient.charge(
                subscription.getBillingKey(),
                subscription.getCustomerKey(),
                orderId,
                "구독 1개월",
                subscription.getPrice()
        );

        Payment payment = Payment.builder()
                .memberId(memberId)
                .subscriptionId(subscription.getSubscriptionId())
                .paymentKey(paymentResponse.paymentKey())
                .orderId(paymentResponse.orderId())
                .orderName(paymentResponse.orderName())
                .status(paymentResponse.status())
                .method(paymentResponse.method())
                .totalAmount(paymentResponse.totalAmount())
                .requestedAt(paymentResponse.requestedAt())
                .approvedAt(paymentResponse.approvedAt())
                .canceledAt(null)
                .build();
        paymentMapper.save(payment);

        return CreateSubscriptionResponse.from(payment);
    }

    @Override
    public PagingSubListResponse getSubscribeCreatorList(
            Long memberId, Long cursor, int size
    ) {
        List<SubscribeCreatorListResponse> sublists
                = subscriptionMapper.findSubscribeCreatorList(memberId, cursor, size + 1);
        int subCreatorCount = subscriptionMapper.countSubscribeCreator(memberId);
        return toPagingSubListResponse(sublists, subCreatorCount, size);
    }

    // 로그인 붙으면 확인절차 로직 만들것
    @Override
    @PreAuthorize("@subscriptionServiceImpl.isAuthor(#subscriptionId, authentication.principal.id)")
    public SubscriptionDetailResponse getSubscriptionDetail(Long subscriptionId) {
        return subscriptionMapper.findSubscriptionDetail(subscriptionId);
    }

    private PagingSubListResponse toPagingSubListResponse(
            List<SubscribeCreatorListResponse> result, int subCreatorCount, int size) {
        boolean hasNext = result.size() > size;

        List<SubscribeCreatorListResponse> subCreatorlist = List.copyOf(
                result.subList(0, Math.min(result.size(), size))
        );

        Long nextCursor = hasNext
                ? subCreatorlist.getLast().subscriptionId()
                : null;

        return new PagingSubListResponse(subCreatorlist, subCreatorCount, nextCursor, hasNext);
    }

    // 본인의 구독 정보가 맞는지 검증하는 헬퍼메소드
    public boolean isAuthor(Long subscriptionId, Long memberId) {
        Subscription sub = subscriptionMapper.findBySubscriptionId(subscriptionId);
        return sub != null && sub.getMemberId().equals(memberId);
    }
}
