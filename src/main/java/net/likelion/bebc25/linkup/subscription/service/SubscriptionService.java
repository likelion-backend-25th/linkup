package net.likelion.bebc25.linkup.subscription.service;

import net.likelion.bebc25.linkup.subscription.domain.Subscription;
import net.likelion.bebc25.linkup.subscription.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubscriptionService {

    CreateSubscriptionResponse createSubscription(
            Long memberId, CreateSubscriptionRequest createSubscriptionRequest
    );

    PagingSubListResponse getSubscribeCreatorList(
            Long memberId, Long cursor, int size
    );

    SubscriptionDetailResponse getSubscriptionDetail(Long subscriptionId);
}
