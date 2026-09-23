package net.likelion.bebc25.linkup.subscription.mapper;

import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscriptionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubscriptionMapper {

    List<SubscriptionResponse> findAll();

    List<SubscriptionResponse> findByCreatorId(@Param("creatorId") Long creatorId);

    List<SubscriptionResponse> findByMemberId(@Param("memberId") Long memberId);

    List<SubscribeCreatorListResponse> findSubscribeCreatorList(@Param("memberId") Long memberId);
}
