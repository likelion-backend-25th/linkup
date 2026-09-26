package net.likelion.bebc25.linkup.reply.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import net.likelion.bebc25.linkup.reply.dto.ReplyPageResponse;
import net.likelion.bebc25.linkup.reply.dto.ReplyResponse;
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
    private final PostMapper postMapper;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public ReplyPageResponse getReplies(Long postId, Long memberId, Long cursor, int size) {
        validatePostReadAccess(postId, memberId);
        List<ReplyResponse> result =
                replyMapper.getByPostId(postId, cursor, size + 1);
        return toReplyResponse(result, size);
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

    private void validatePostReadAccess(Long postId, Long memberId) {
        Post post = postMapper.findById(postId);

        if (post == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."
            );
        }

        Long AuthorId = post.getMemberId();

        if (post.isSubscriberOnly()) {
            // 구독 테이블에 존재하는 지 검증
        }


    }
}
