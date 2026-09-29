package net.likelion.bebc25.linkup.follow.dto;

public record FollowResponse (
        Long targetId,
        boolean isFollowing,
        int followerCount,
        Long memberId,
        int followingCount
) {}
