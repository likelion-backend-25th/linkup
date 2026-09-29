package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.post.mapper.FeedMapper;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedServiceImpl implements FeedService {
    private final FeedMapper feedMapper;
    private final PostMapper postMapper;

    @Override
    public FeedResponse getFollowingFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                feedMapper.findFollowingFeed(memberId, cursor, size + 1);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getSubscriptionFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                feedMapper.findSubscriptionFeed(memberId, cursor, size + 1);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getMyFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                feedMapper.findFeedByMemberId(memberId, cursor, size + 1);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getMySubscriberOnlyFeed(Long memberId, Long cursor, int size) {
        List<PostCardResponse> result =
                feedMapper.findSubscriberOnlyFeedByMemberId(memberId, cursor, size + 1);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getTargetFeed(Long targetId, Long cursor, int size) {
        List<PostCardResponse> result =
                feedMapper.findFeedByMemberId(targetId, cursor, size + 1);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getTargetSubscriberOnlyFeed(Long memberId, Long targetId, Long cursor, int size) {
        if (postMapper.existsValidSubscription(memberId, targetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "구독하지 않은 회원입니다.");
        }
        List<PostCardResponse> result =
                feedMapper.findSubscriberOnlyFeedByMemberId(targetId, cursor, size + 1);
        return toFeedResponse(result, size);
    }

    @Override
    public FeedResponse getPopularFeed(Integer cursorLikeCount, Long cursorPostId, int size) {
        if ((cursorLikeCount == null) != (cursorPostId == null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "cursorLikeCount와 cursorPostId는 함께 전달해야 합니다."
            );
        }

        List<PostCardResponse> result =
                feedMapper.findPopularFeed(cursorLikeCount, cursorPostId, size + 1);
        return toFeedResponse(result, size);
    }

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
}
