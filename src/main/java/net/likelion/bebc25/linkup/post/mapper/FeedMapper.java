package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.dto.PostFeedResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface FeedMapper {

    // 내가 팔로잉한 회원들 피드 조회
    List<PostFeedResponse> findFollowingFeed(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit
    );
}
