package net.likelion.bebc25.linkup.subscription.mapper;

import net.likelion.bebc25.linkup.subscription.domain.Subscription;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscriptionDetailResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscriptionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubscriptionMapper {

    int save(Subscription subscription);

    int updateCancel(@Param("subscriptionId") Long subscriptionId);

    int updateNextBillingAt(@Param("subscriptionId") Long subscriptionId);

    int deleteExpirations();

    String findByMemberIdAndCreatorId(@Param("memberId") Long memberId, @Param("creatorId") Long creatorId) ;

    Subscription findBySubscriptionId(@Param("subscriptionId") Long subscriptionId);

    SubscriptionDetailResponse findSubscriptionDetail(@Param("subscriptionId") Long subscriptionId);

    List<Subscription> findAutoPaymentRenewalList();

    List<SubscribeCreatorListResponse> findSubscribeCreatorList(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

    int countSubscribeCreator(@Param("memberId") Long memberId);
}
