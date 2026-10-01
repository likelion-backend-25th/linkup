package net.likelion.bebc25.linkup.creator.service;

import net.likelion.bebc25.linkup.creator.dto.PagingSubscriberListResponse;

public interface CreatorService {
    PagingSubscriberListResponse getSubscriberList(
            Long creatorId, Long cursor, int size
    );

    void applyCreator(Long memberId);
}
