package net.likelion.bebc25.linkup.subscription.service;

import net.likelion.bebc25.linkup.subscription.dto.PagingSubListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubscriptionService {
    PagingSubListResponse getSubscribeCreatorList(
            Long memberId, Long cursor, int size
    );
}
