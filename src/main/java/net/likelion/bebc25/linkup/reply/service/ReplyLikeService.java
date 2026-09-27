package net.likelion.bebc25.linkup.reply.service;

import org.apache.ibatis.annotations.Param;

public interface ReplyLikeService {
    // 댓글 좋아요 등록
    void likeReply(
            @Param("postId") Long postId,
            @Param("replyId") Long replyId,
            @Param("memberId") Long memberId);

    // 댓글 좋아요 삭제
    void unlikeReply(
            @Param("postId") Long postId,
            @Param("replyId") Long replyId,
            @Param("memberId") Long memberId);
}
