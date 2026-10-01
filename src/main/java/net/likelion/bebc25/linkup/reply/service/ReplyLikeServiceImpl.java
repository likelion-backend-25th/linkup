package net.likelion.bebc25.linkup.reply.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
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
    private final PostMapper postMapper;

    @Transactional
    @Override
    public void likeReply(Long postId, Long replyId, Long memberId) {
        validatePostReadAccess(postId, memberId);
        validateReplyInPost(postId, replyId);
        validateReplyNotLiked(replyId, memberId);

        replyLikeMapper.insert(replyId, memberId);
        replyMapper.incrementLikeCount(replyId);
    }

    @Transactional
    @Override
    public void unlikeReply(Long postId, Long replyId, Long memberId) {
        validatePostReadAccess(postId, memberId);
        validateReplyInPost(postId, replyId);

        int deleteCount = replyLikeMapper.delete(replyId, memberId);
        if (deleteCount == 1) {
            replyMapper.decrementLikeCount(replyId);
        }
    }
    // 구독자 전용 게시글은 작성자 또는 유효한 구독자만 댓글 좋아요를 누를 수 있다.
    private void validatePostReadAccess(Long postId, Long memberId) {
        Post post = postMapper.findById(postId, memberId);

        if (post == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게시글이 존재하지 않습니다."
            );
        }

        if (!post.isSubscriberOnly()) {
            return;
        }

        Long authorId = post.getMemberId();

        if (memberId.equals(authorId)) {
            return;
        }

        if (!postMapper.existsValidSubscription(memberId, authorId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "구독하지 않은 게시글입니다."
            );
        }
    }

    // 댓글이 해당 게시글에 존재하는지 확인한다.
    private void validateReplyInPost(Long postId, Long replyId) {
        Reply reply = replyMapper.getById(replyId);

        if (reply == null || !reply.getPostId().equals(postId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 댓글입니다."
            );
        }
    }

    // 이미 좋아요한 댓글에는 다시 좋아요할 수 없다.
    private void validateReplyNotLiked(Long replyId, Long memberId) {
        if (replyLikeMapper.existsByReplyIdAndMemberId(replyId, memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 좋아요가 등록되어 있습니다."
            );
        }
    }
}
