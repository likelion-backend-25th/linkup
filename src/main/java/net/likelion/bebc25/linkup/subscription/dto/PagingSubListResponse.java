package net.likelion.bebc25.linkup.subscription.dto;

import java.util.List;

public record PagingSubListResponse(
        List<SubscribeCreatorListResponse> subCreatorList,
        int subCreatorCount,
        Long nextCursor,
        boolean hasNext
) {

}
