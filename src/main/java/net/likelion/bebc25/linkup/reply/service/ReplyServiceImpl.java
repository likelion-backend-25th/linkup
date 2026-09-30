package net.likelion.bebc25.linkup.reply.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.block.mapper.BlockMapper;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import net.likelion.bebc25.linkup.reply.domain.Reply;
import net.likelion.bebc25.linkup.reply.dto.ReplyCreateRequest;
import net.likelion.bebc25.linkup.reply.dto.ReplyPageResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyUpdateRequest;
import net.likelion.bebc25.linkup.reply.mapper.ReplyLikeMapper;
import net.likelion.bebc25.linkup.reply.mapper.ReplyMapper;
import net.likelion.bebc25.linkup.subscription.mapper.SubscriptionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyServiceImpl implements ReplyService {

    private final ReplyMapper replyMapper;
    private final ReplyLikeMapper replyLikeMapper;
    private final PostMapper postMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final BlockMapper blockMapper;

    @Override
    public ReplyPageResponse getReplies(Long postId, Long memberId, Long cursor, int size) {
        validatePostReadAccess(postId, memberId);
        List<ReplyResponse> result =
                replyMapper.getByPostId(postId, memberId, cursor, size + 1)
                        .stream()
                        .map(reply -> withLikedByMe(reply, memberId))
                        .toList();
        return toReplyResponse(result, size);
    }

    @Transactional
    @Override
    public Long createReply(Long postId, Long memberId, ReplyCreateRequest request) {
        validatePostReadAccess(postId, memberId);

        Reply reply = createReplyEntity(postId, memberId, request);
        replyMapper.insert(reply);

        return reply.getId();
    }

    @Transactional
    @Override
    public void deleteReply(Long postId, Long memberId, Long replyId) {
        validateReplyDeletePermission(postId, memberId, replyId);
        replyMapper.deleteById(replyId);
    }

    @Transactional
    @Override
    public void updateReply(Long postId, Long memberId, Long replyId, ReplyUpdateRequest request) {
        validateReplyUpdatePermission(postId, memberId, replyId);
        replyMapper.updateReplyById(replyId, request.content());
    }

    private ReplyPageResponse toReplyResponse(List<ReplyResponse> result, int size) {
        boolean hasNext = result.size() > size;

        List<ReplyResponse> replies = List.copyOf(
                result.subList(0, Math.min(result.size(), size))
        );

        Long nextCursor = hasNext
                ? replies.getLast().id()
                : null;

        return new ReplyPageResponse(replies, nextCursor, hasNext);
    }

    private ReplyResponse withLikedByMe(ReplyResponse reply, Long memberId) {
        boolean likedByMe = replyLikeMapper.existsByReplyIdAndMemberId(reply.id(), memberId);

        return new ReplyResponse(
                reply.id(),
                reply.memberId(),
                reply.name(),
                reply.uniqueId(),
                reply.profileImage(),
                reply.content(),
                reply.likeCount(),
                likedByMe,
                reply.createdAt(),
                reply.updatedAt()
        );
    }

    // 게시글이 존재하는지 확인하고 조회한다.
    private Post getPostOrThrow(Long postId) {
        Post post = postMapper.findById(postId);

        if (post == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게시글이 존재하지 않습니다."
            );
        }

        return post;
    }

    // 구독자 전용 게시글은 작성자 또는 유효한 구독자만 댓글을 조회/작성할 수 있다.
    private void validatePostReadAccess(Long postId, Long memberId) {
        Post post = getPostOrThrow(postId);
        Long authorId = post.getMemberId();

        if (!memberId.equals(authorId) && blockMapper.existsBlock(memberId, authorId) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게시글이 존재하지 않습니다."
            );
        }

        if (!post.isSubscriberOnly()) {
            return;
        }

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

    // 댓글 작성자만 댓글을 수정할 수 있다.
    private void validateReplyUpdatePermission(Long postId, Long memberId, Long replyId) {
        Reply reply = getReplyInPostOrThrow(postId, replyId);

        if (!reply.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "댓글 작성자가 아닙니다."
            );
        }
    }

    // 게시글 작성자 또는 댓글 작성자만 댓글을 삭제할 수 있다.
    private void validateReplyDeletePermission(Long postId, Long memberId, Long replyId) {
        Reply reply = getReplyInPostOrThrow(postId, replyId);
        Post post = getPostOrThrow(postId);

        if (post.getMemberId().equals(memberId) || reply.getMemberId().equals(memberId)) {
            return;
        }

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "댓글을 삭제할 권한이 없습니다."
        );
    }
    // 댓글이 해당 게시글에 존재하는지 확인하고 조회한다.
    private Reply getReplyInPostOrThrow(Long postId, Long replyId) {
        Reply reply = replyMapper.getById(replyId);

        if (reply == null || !reply.getPostId().equals(postId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 댓글입니다."
            );
        }

        return reply;
    }

    private Reply createReplyEntity(Long postId, Long memberId, ReplyCreateRequest request) {
        return Reply.builder()
                .postId(postId)
                .memberId(memberId)
                .content(request.content())
                .build();
    }
}
