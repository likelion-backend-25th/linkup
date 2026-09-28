package net.likelion.bebc25.linkup.subscription.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import net.likelion.bebc25.linkup.subscription.mapper.SubscriptionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public List<SubscribeCreatorListResponse> getSubscribeCreatorList(Long memberId) {
        return subscriptionMapper.findSubscribeCreatorList(memberId);
    }
}
