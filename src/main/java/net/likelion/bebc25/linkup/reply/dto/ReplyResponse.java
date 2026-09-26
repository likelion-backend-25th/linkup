package net.likelion.bebc25.linkup.reply.dto;

import java.time.LocalDateTime;

public record ReplyResponse (
    Long id,
    Long memberId,
    String name,
    String uniqueId,
    String profileImage,
    String content,
    int likeCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){
}
