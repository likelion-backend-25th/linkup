package net.likelion.bebc25.linkup.reply.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReplyLikeMapper {
    // 댓글 좋아요 등록
    int insert(@Param("replyId") Long replyId, @Param("memberId") Long memberId);

    // 댓글 좋아요 취소
    int delete(@Param("replyId") Long replyId, @Param("memberId") Long memberId);

    // 댓글 좋아요 존재 여부 조회
    boolean existsByReplyIdAndMemberId(@Param("replyId") Long replyId, @Param("memberId") Long memberId);
}
