package net.likelion.bebc25.linkup.post.dto;

import java.util.List;

public record FollowingFeedResponse (
        List<PostFeedResponse> posts,
        Long nextCursor,
        boolean hasNext
) {
}
