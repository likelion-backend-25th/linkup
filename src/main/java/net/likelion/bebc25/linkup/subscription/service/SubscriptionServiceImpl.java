package net.likelion.bebc25.linkup.subscription.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.subscription.domain.Subscription;
import net.likelion.bebc25.linkup.subscription.dto.*;
import net.likelion.bebc25.linkup.subscription.mapper.SubscriptionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public CreateSubscriptionResponse createSubscription(
            Long memberId, CreateSubscriptionRequest createSubscriptionRequest
    ) {
        Subscription subscription = Subscription.builder()
                .memberId(memberId)
                .creatorId(createSubscriptionRequest.creatorId())
                .customerUid(createSubscriptionRequest.customerUid())
                .price(createSubscriptionRequest.price())
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
}
