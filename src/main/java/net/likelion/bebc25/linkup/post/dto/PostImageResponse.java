package net.likelion.bebc25.linkup.post.dto;

import net.likelion.bebc25.linkup.post.domain.PostImage;

public record PostImageResponse(
        Long id,
        String imageUrl,
        int imageOrder
) {
    public static PostImageResponse from(PostImage image) {
        return new PostImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getImageOrder()
        );
    }
}
