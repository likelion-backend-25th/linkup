package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.FollowingFeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostFeedResponse;
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
    public FollowingFeedResponse getFollowingFeed(Long id, Long cursor, int size) {
        int limit = size + 1;

        List<PostFeedResponse> result = feedMapper.findFollowingFeed(id, cursor, limit);

        boolean hasNext = result.size() > size;

        List<PostFeedResponse> posts = hasNext ? List.copyOf(result.subList(0, size)) : result;

        Long nextCursor = hasNext ? posts.get(posts.size() - 1).postId() : null;

        return new FollowingFeedResponse(
                posts,
                nextCursor,
                hasNext
        );
    }
}
