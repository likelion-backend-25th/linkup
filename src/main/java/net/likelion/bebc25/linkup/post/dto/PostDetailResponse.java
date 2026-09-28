package net.likelion.bebc25.linkup.post.dto;

import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.domain.PostImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PostDetailResponse (
        Long id,
        Long memberId,
        String name,
        String uniqueId,
        String profileImage,
        String content,
        String fileUrl,
        int likeCount,
        boolean subscriberOnly,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean likedByMe,
        List<PostImageResponse> images
) {
    public static PostDetailResponse from(PostDetailRow postDetailRow, List<PostImage> images, boolean likedByMe) {
        List<PostImageResponse> imageResponses = images.stream()
                .map(PostImageResponse::from)
                .toList();

        return new PostDetailResponse(
                postDetailRow.getId(),
                postDetailRow.getMemberId(),
                postDetailRow.getName(),
                postDetailRow.getUniqueId(),
                postDetailRow.getProfileImage(),
                postDetailRow.getContent(),
                postDetailRow.getFileUrl(),
                postDetailRow.getLikeCount(),
                postDetailRow.isSubscriberOnly(),
                postDetailRow.getCreatedAt(),
                postDetailRow.getUpdatedAt(),
                likedByMe,
                imageResponses
        );
    }
}
