package net.likelion.bebc25.linkup.reply.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.reply.domain.Reply;
import net.likelion.bebc25.linkup.reply.mapper.ReplyLikeMapper;
import net.likelion.bebc25.linkup.reply.mapper.ReplyMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyLikeServiceImpl implements ReplyLikeService {

    private final ReplyLikeMapper replyLikeMapper;
    private final ReplyMapper replyMapper;

    @Transactional
    @Override
    public void likeReply(Long postId, Long replyId, Long memberId) {
        validateLikeRegistration(postId, replyId, memberId);
        replyLikeMapper.insert(replyId, memberId);
        replyMapper.incrementLikeCount(replyId);
    }

    @Transactional
    @Override
    public void unlikeReply(Long postId, Long replyId, Long memberId) {
        validateLikeDeletion(postId, replyId, memberId);
        int deleteCount = replyLikeMapper.delete(replyId, memberId);
        if (deleteCount == 1) {
            replyMapper.decrementLikeCount(replyId);
        }
    }

    // 좋아요 등록 검증
    private void validateLikeRegistration(Long postId, Long replyId, Long memberId) {
        Reply reply = replyMapper.getById(replyId);
        if (reply == null || !reply.getPostId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다.");
        }

        if (replyLikeMapper.existsByReplyIdAndMemberId(replyId, memberId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 좋아요가 등록되어 있습니다.");
        }
    }

    // 좋아요 삭제 검증
    private void validateLikeDeletion(Long postId, Long replyId, Long memberId) {
        Reply reply = replyMapper.getById(replyId);
        if (reply == null || !reply.getPostId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다.");
        }
    }
}
