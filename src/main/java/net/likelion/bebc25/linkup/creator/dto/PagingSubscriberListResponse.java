package net.likelion.bebc25.linkup.creator.dto;

import java.util.List;

public record PagingSubscriberListResponse(
        List<SubscriberResponse> subscriberList,
        int subscriberCount,
        Long nextCursor,
        boolean hasNext
) {
}
