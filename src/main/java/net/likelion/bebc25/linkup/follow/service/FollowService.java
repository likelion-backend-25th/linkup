package net.likelion.bebc25.linkup.follow.service;

import net.likelion.bebc25.linkup.follow.dto.FollowResponse;

public interface FollowService {

    // 팔로우
    void addFollow(Long memberId, Long targetId);

    // 언팔로우
    void deleteFollow(Long memberId, Long targetId);

    // 팔로우 상태 조회
    boolean isFollowing(Long memberId, Long targetId);

    // 팔로워 수
    int countFollowers(Long targetId);

    // 팔로잉 수
    int countFollowings(Long memberId);

    // 팔로우 상태 및 팔로워 수 조회
    FollowResponse getFollowStatus(
            Long memberId,
            Long targetId
    );
}