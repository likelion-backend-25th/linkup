package net.likelion.bebc25.linkup.subscription.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.subscription.dto.PagingSubListResponse;
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
    public PagingSubListResponse getSubscribeCreatorList(
            Long memberId, Long cursor, int size
    ) {
        List<SubscribeCreatorListResponse> sublists
                = subscriptionMapper.findSubscribeCreatorList(memberId, cursor, size + 1);
        return toPagingSubListResponse(sublists, size);
    }

    private PagingSubListResponse toPagingSubListResponse(List<SubscribeCreatorListResponse> result, int size) {
        boolean hasNext = result.size() > size;

        List<SubscribeCreatorListResponse> subCreatorlist = List.copyOf(
                result.subList(0, Math.min(result.size(), size))
        );

        Long nextCursor = hasNext
                ? subCreatorlist.getLast().subscriptionId()
                : null;

        return new PagingSubListResponse(subCreatorlist, nextCursor, hasNext);
    }
}
