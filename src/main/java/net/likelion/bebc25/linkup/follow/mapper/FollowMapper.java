package net.likelion.bebc25.linkup.follow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowMapper {

    // 팔로우 추가
    int addFollow (
            @Param("memberId") Long memberId,
            @Param("targetId") Long targetId
    );

    // 팔로잉 증가
    void incrementFollowingCount (
            @Param("memberId") Long memberId
    );

    // 팔로워 증가
    void incrementFollowerCount (
            @Param("memberId") Long memberId
    );

    // 팔로우 취소 (언팔로우)
    int deleteFollow(
            @Param("memberId") Long memberId,
            @Param("targetId") Long targetId
    );

    // 팔로우 상태 조회
    boolean isFollowing(
            @Param("memberId") Long memberId,
            @Param("targetId") Long targetId
    );

    // 팔로워 수 확인 (조회)
    int countFollowers(
            @Param("targetId") Long targetId
    );

    // 팔로잉 수 확인 (조회)
    int countFollowings(
            @Param("memberId") Long memberId
    );
}

