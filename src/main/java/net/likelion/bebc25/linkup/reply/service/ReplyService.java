package net.likelion.bebc25.linkup.reply.service;

import net.likelion.bebc25.linkup.reply.dto.ReplyCreateRequest;
import net.likelion.bebc25.linkup.reply.dto.ReplyPageResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyUpdateRequest;

public interface ReplyService {
    // 댓글 목록 조회
    ReplyPageResponse getReplies(Long postId, Long memberId, Long cursor, int size);

    // 댓글 생성
    Long createReply(Long postId, Long memberId, ReplyCreateRequest request);

    // 댓글 삭제
    void deleteReply(Long postId, Long memberId, Long replyId);

    // 댓글 수정
    void updateReply(Long postId, Long memberId, Long replyId, ReplyUpdateRequest request);
}
