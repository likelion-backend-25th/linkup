package net.likelion.bebc25.linkup.follow.mapper;

import net.likelion.bebc25.linkup.follow.dto.FollowMemberResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    // 팔로잉 감소
    void decrementFollowingCount (
            @Param("memberId") Long memberId
    );

    // 팔로워 감소
    void decrementFollowerCount (
            @Param("memberId") Long memberId
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

    // 팔로워 목록 조회

    List<FollowMemberResponse> findFollowers(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

    List<FollowMemberResponse> findFollowings(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

}

