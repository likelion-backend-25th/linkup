package net.likelion.bebc25.linkup.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Post {
    private Long id;
    private Long memberId;
    private String content;
    private String fileUrl;
    private int likeCount;
    private boolean subscriberOnly;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
