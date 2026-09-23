package net.likelion.bebc25.linkup.post.dto;

import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.domain.PostImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PostDetailResponse (
        Long id,
        Long memberId,
        String content,
        String fileUrl,
        int likeCount,
        boolean subscriberOnly,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<PostImageResponse> images
) {
    public static PostDetailResponse from(Post post, List<PostImage> images) {
        List<PostImageResponse> imageResponses = images.stream()
                .map(PostImageResponse::from)
                .toList();

        return new PostDetailResponse(
                post.getId(),
                post.getMemberId(),
                post.getContent(),
                post.getFileUrl(),
                post.getLikeCount(),
                post.isSubscriberOnly(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                imageResponses
        );
    }
}
