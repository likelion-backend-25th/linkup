package net.likelion.bebc25.linkup.subscription.service;

import net.likelion.bebc25.linkup.subscription.dto.*;
import org.apache.ibatis.annotations.Param;

public interface SubscriptionService {

    CancelSubscriptionResponse cancelSubscription(Long subscriptionId);

    RefundSubscriptionResponse refundSubscription(Long memberId, Long subscriptionId);

    int deleteExpiredSubscriptions();

    int autoPayment();

    CheckBillingDateResponse checkBillingDate(@Param("subscriptionId") Long subscriptionId);

    CreateSubscriptionResponse createSubscription(
            Long memberId, CreateSubscriptionRequest createSubscriptionRequest
    );

    PagingSubListResponse getSubscribeCreatorList(
            Long memberId, Long cursor, int size
    );

    SubscriptionDetailResponse getSubscriptionDetail(Long subscriptionId);
}
