package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface FeedMapper {

    // 내가 팔로잉한 회원들 피드 조회
    List<PostCardResponse> findFollowingFeed(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

    // 내가 구독한 크리에이터 피드 조회
    List<PostCardResponse> findSubscriptionFeed(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

    // 내 전체 공개 게시글 피드 조회
    List<PostCardResponse> findFeedByMemberId(
            @Param("targetMemberId") Long targetMemberId,
            @Param("viewerId") Long viewerId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

    // 내 구독자 전용 게시글 피드 조회
    List<PostCardResponse> findSubscriberOnlyFeedByMemberId(
            @Param("targetMemberId") Long targetMemberId,
            @Param("viewerId") Long viewerId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );

    // 전체 게시글 좋아요 순으로 피드 조회
    List<PostCardResponse> findPopularFeed(
            @Param("memberId") Long memberId,
            @Param("cursorLikeCount") Integer cursorLikeCount,
            @Param("cursorPostId") Long cursorPostId,
            @Param("limit") int limit
    );
}
