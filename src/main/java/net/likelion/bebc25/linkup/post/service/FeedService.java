package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.FollowingFeedResponse;

public interface FeedService {
    // 내가 팔로잉한 회원들 게시글 조회
    FollowingFeedResponse getFollowingFeed(Long id, Long cursor, int size);
}
