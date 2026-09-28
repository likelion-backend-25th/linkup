package net.likelion.bebc25.linkup.subscription.service;

import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubscriptionService {
    List<SubscribeCreatorListResponse> getSubscribeCreatorList(@Param("memberId") Long memberId);
}
