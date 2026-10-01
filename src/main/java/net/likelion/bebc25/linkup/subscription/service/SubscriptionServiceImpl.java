package net.likelion.bebc25.linkup.subscription.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.payment.client.TossPaymentClient;
import net.likelion.bebc25.linkup.payment.dto.BillingKeyResponse;
import net.likelion.bebc25.linkup.subscription.domain.Subscription;
import net.likelion.bebc25.linkup.subscription.dto.*;
import net.likelion.bebc25.linkup.subscription.mapper.SubscriptionMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionMapper subscriptionMapper;
    private final TossPaymentClient tossPaymentClient;

    @Override
    @PreAuthorize("@SubscriptionServiceImpl.isAuthor(#subscriptionId, authentication.principal.id)")
    public int cancelSubscription(Long subscriptionId) {
        return subscriptionMapper.updateCancel(subscriptionId);
    }

    @Override
    @Scheduled(fixedDelay = 60_000) // 1분 간격
    public int deleteExpiredSubscriptions() {
        return subscriptionMapper.deleteExpirations();
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
        return CreateSubscriptionResponse.from(subscription);
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
    @PreAuthorize("@SubscriptionServiceImpl.isAuthor(#subscriptionId, authentication.principal.id)")
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
