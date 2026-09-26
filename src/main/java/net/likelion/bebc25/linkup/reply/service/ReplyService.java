package net.likelion.bebc25.linkup.reply.service;

import net.likelion.bebc25.linkup.reply.dto.ReplyPageResponse;

import java.util.List;

public interface ReplyService {
    // 댓글 목록 조회
    ReplyPageResponse getReplies(Long postId, Long memberId, Long cursor, int size);
}
