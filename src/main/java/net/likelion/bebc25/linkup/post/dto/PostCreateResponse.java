package net.likelion.bebc25.linkup.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import net.likelion.bebc25.linkup.post.domain.Post;

public record PostCreateResponse(
        @Schema(description = "게시글 고유 식별자")
        Long id
) {
        public static PostCreateResponse from(Post post) {
                return new PostCreateResponse(post.getId());
        }
}
