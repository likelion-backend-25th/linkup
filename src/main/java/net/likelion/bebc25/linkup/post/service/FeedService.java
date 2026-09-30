package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.FeedResponse;

public interface FeedService {
    // 내가 팔로잉한 회원들 게시글 조회
    FeedResponse getFollowingFeed(Long memberId, Long cursor, int size);

    // 내가 구독한 크리에이터 게시글 조회
    FeedResponse getSubscriptionFeed(Long memberId, Long cursor, int size);

    // 내 전체 공개 게시글 조회
    FeedResponse getMyFeed(Long memberId, Long cursor, int size);

    // 내 구독자 전용 게시글 조회
    FeedResponse getMySubscriberOnlyFeed(Long memberId, Long cursor, int size);

    // 특정 회원의 게시글 조회
    FeedResponse getTargetFeed(Long memberId, Long targetId, Long cursor, int size);

    // 특정 회원의 구독자 전용 게시글 조회
    FeedResponse getTargetSubscriberOnlyFeed(Long memberId, Long targetId, Long cursor, int size);

    // 전체 공개 게시글 좋아요 순 피드 조회
    FeedResponse getPopularFeed(Long memberId, Integer cursorLikeCount, Long cursorPostId, int size);
}
