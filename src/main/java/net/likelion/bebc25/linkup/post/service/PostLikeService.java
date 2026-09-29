package net.likelion.bebc25.linkup.post.service;

import org.apache.ibatis.annotations.Param;

public interface PostLikeService {
    // 게시글 좋아요 등록
    void likePost(
            @Param("postId") Long postId,
            @Param("memberId") Long memberId
    );

    // 게시글 좋아요 삭제
    void unlikePost(
            @Param("postId") Long postId,
            @Param("memberId") Long memberId
    );
}
