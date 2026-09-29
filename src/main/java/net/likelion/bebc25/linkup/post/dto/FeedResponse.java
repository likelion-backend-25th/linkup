package net.likelion.bebc25.linkup.post.dto;

import java.util.List;

public record FeedResponse(
        List<PostCardResponse> posts,
        Long nextCursor,
        boolean hasNext
) {
}
