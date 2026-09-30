package net.likelion.bebc25.linkup.creator.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.creator.dto.PagingSubscriberListResponse;
import net.likelion.bebc25.linkup.creator.dto.SubscriberResponse;
import net.likelion.bebc25.linkup.creator.mapper.CreatorMapper;
import net.likelion.bebc25.linkup.subscription.dto.PagingSubListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorServiceImpl implements CreatorService{
    private final CreatorMapper creatorMapper;

    @Override
    public PagingSubscriberListResponse getSubscriberList(
            Long creatorId, Long cursor, int size
    ) {
        List<SubscriberResponse> sublists
                = creatorMapper.findSubscriberList(creatorId, cursor, size + 1);
        int subscriberCount = creatorMapper.countSubscriber(creatorId);
        return toPagingSubscriberListResponse(sublists, subscriberCount, size);
    }

    private PagingSubscriberListResponse toPagingSubscriberListResponse(
            List<SubscriberResponse> result, int subscriberCount, int size
    ) {
        boolean hasNext = result.size() > size;

        List<SubscriberResponse> subscriberList = List.copyOf(
                result.subList(0, Math.min(result.size(), size))
        );

        Long nextCursor = hasNext
                ? subscriberList.getLast().subscriptionId()
                : null;

        return new PagingSubscriberListResponse(subscriberList, subscriberCount, nextCursor, hasNext);
    }
}
