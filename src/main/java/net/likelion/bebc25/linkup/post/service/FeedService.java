package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.FeedResponse;

public interface FeedService {
    // 내가 팔로잉한 회원들 게시글 조회
    FeedResponse getFollowingFeed(Long memberId, Long cursor, int size);

    // 내가 구독한 크리에이터 게시글 조회
    FeedResponse getSubscriptionFeed(Long memberId, Long cursor, int size);
}
