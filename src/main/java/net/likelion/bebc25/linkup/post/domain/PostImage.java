package net.likelion.bebc25.linkup.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostImage {
    private Long id;
    private Long postId;
    private String imageUrl;
    private int imageOrder;
    private LocalDateTime createdAt;
}
