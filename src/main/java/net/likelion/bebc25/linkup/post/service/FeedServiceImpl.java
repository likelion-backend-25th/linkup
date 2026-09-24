package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.post.mapper.FeedMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedServiceImpl implements FeedService {
    private final FeedMapper feedMapper;

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

    private FeedResponse toFeedResponse(List<PostCardResponse> result, int size) {
        boolean hasNext = result.size() > size;

        List<PostCardResponse> posts = List.copyOf(
                result.subList(0, size)
        );

        Long nextCursor = hasNext
                ? posts.getLast().postId()
                : null;

        return new FeedResponse(posts, nextCursor, hasNext);
    }
}
