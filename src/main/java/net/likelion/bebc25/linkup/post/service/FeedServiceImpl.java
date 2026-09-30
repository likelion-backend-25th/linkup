package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.post.mapper.FeedMapper;
import net.likelion.bebc25.linkup.post.mapper.PostLikeMapper;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedServiceImpl implements FeedService {
    private final FeedMapper feedMapper;
    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;

    @Override
    public FeedResponse getFollowingFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                withLikedByMe(feedMapper.findFollowingFeed(memberId, cursor, size + 1), memberId);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getSubscriptionFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                withLikedByMe(feedMapper.findSubscriptionFeed(memberId, cursor, size + 1), memberId);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getMyFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                withLikedByMe(feedMapper.findFeedByMemberId(memberId, cursor, size + 1), memberId);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getMySubscriberOnlyFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                withLikedByMe(feedMapper.findSubscriberOnlyFeedByMemberId(memberId, cursor, size + 1), memberId);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getTargetFeed(Long memberId, Long targetId, Long cursor, int size) {
        List<PostCardResponse> result =
                withLikedByMe(feedMapper.findFeedByMemberId(targetId, cursor, size + 1), memberId);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getTargetSubscriberOnlyFeed(Long memberId, Long targetId, Long cursor, int size) {
        validateSubscriberOnlyFeedAccess(memberId, targetId);

        List<PostCardResponse> result =
                withLikedByMe(feedMapper.findSubscriberOnlyFeedByMemberId(targetId, cursor, size + 1), memberId);

        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getPopularFeed(Long memberId, Integer cursorLikeCount, Long cursorPostId, int size) {
        if ((cursorLikeCount == null) != (cursorPostId == null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "cursorLikeCount와 cursorPostId는 함께 전달해야 합니다."
            );
        }

        List<PostCardResponse> result =
                withLikedByMe(feedMapper.findPopularFeed(cursorLikeCount, cursorPostId, size + 1), memberId);
        return toFeedResponse(result, size);
    }

    // limit보다 한 개 더 조회한 결과로 다음 페이지 존재 여부를 판단한다.
    private FeedResponse toFeedResponse(List<PostCardResponse> result, int size) {
        boolean hasNext = result.size() > size;

        List<PostCardResponse> posts = List.copyOf(
                result.subList(0, Math.min(result.size(), size))
        );

        Long nextCursor = hasNext
                ? posts.getLast().postId()
                : null;

        return new FeedResponse(posts, nextCursor, hasNext);
    }

    private List<PostCardResponse> withLikedByMe(List<PostCardResponse> posts, Long memberId) {
        if (memberId == null) {
            return posts;
        }

        return posts.stream()
                .map(post -> withLikedByMe(post, memberId))
                .toList();
    }

    private PostCardResponse withLikedByMe(PostCardResponse post, Long memberId) {
        boolean likedByMe = postLikeMapper.existsByPostIdAndMemberId(post.postId(), memberId);

        return new PostCardResponse(
                post.postId(),
                post.memberId(),
                post.memberName(),
                post.uniqueId(),
                post.profileImageUrl(),
                post.content(),
                post.mainImageUrl(),
                post.likeCount(),
                post.commentCount(),
                likedByMe,
                post.subscriberOnly(),
                post.createdAt()
        );
    }

    // 구독자 전용 피드는 작성자 본인 또는 유효한 구독자만 조회할 수 있다.
    private void validateSubscriberOnlyFeedAccess(Long memberId, Long targetId) {
        if (memberId.equals(targetId)) {
            return;
        }

        if (!postMapper.existsValidSubscription(memberId, targetId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "구독하지 않은 회원입니다."
            );
        }
    }
}
