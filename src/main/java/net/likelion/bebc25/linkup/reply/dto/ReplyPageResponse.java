package net.likelion.bebc25.linkup.reply.dto;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;

import java.util.List;

public record ReplyPageResponse (
        List<ReplyResponse> replies,
        Long nextCursor,
        boolean hasNext
){
}
