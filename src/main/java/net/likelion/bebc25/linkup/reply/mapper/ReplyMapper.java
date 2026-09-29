package net.likelion.bebc25.linkup.reply.mapper;

import net.likelion.bebc25.linkup.reply.domain.Reply;
import net.likelion.bebc25.linkup.reply.dto.ReplyResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper {
    // 댓글 조회
    List<ReplyResponse> getByPostId(
            @Param("postId") Long postId,
            @Param("cursor") Long cursor,
            @Param("limit") int limit);

    // 댓글 식별자로 조회
    Reply getById(@Param("id") Long id);

    // 댓글 생성
    int insert(Reply reply);

    // 댓글 삭제
    int deleteById(@Param("id") Long id);

    // 댓글 수정
    int updateReplyById(@Param("id") Long id, @Param("content") String content);

    // 댓글 좋아요 증가
    int incrementLikeCount(@Param("replyId") Long replyId);

    // 댓글 좋아요 감소
    int decrementLikeCount(@Param("replyId") Long replyId);
}
