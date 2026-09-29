package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.dto.PostDetailRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {
    // 게시글 등록
    int insert(Post post);

    // 게시글 단 건 조회
    Post findById(@Param("postId") Long postId);

    // 게시글 상세 조회
    PostDetailRow findDetailById(@Param("postId") Long postId);

    // 게시글 삭제
    int deleteById(@Param("postId") Long postId);

    // 게시글 수정
    int updateById(Post post);

    // 게시글 좋아요 카운트 증가
    int incrementLikeCount(@Param("postId") Long postId);

    // 게시글 좋아요 카운트 감소
    int decrementLikeCount(@Param("postId") Long postId);

    // 임시 구독 상태 조회.
    boolean existsValidSubscription(@Param("memberId") Long memberId, @Param("creatorId") Long creatorId);
}
