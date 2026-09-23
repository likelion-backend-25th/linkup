package net.likelion.bebc25.linkup.post.dto;

import java.time.LocalDateTime;

public record PostFeedResponse (
        Long postId,
        Long memberId,
        String memberName,
        String uniqueId,
        String profileImageUrl,
        String content,
        String mainImageUrl,
        int likeCount,
        int commentCount,
        boolean subscriberOnly,
        LocalDateTime createdAt
) {
}
