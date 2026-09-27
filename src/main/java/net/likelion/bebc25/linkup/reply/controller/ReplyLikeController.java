package net.likelion.bebc25.linkup.reply.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import net.likelion.bebc25.linkup.reply.service.ReplyLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/replies/{replyId}/likes")
public class ReplyLikeController {
    private final ReplyLikeService replyLikeService;

    @PostMapping
    public ResponseEntity<Void> insertReplyLike(
            @PathVariable long postId,
            @PathVariable long replyId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = 1L;
        replyLikeService.likeReply(postId, replyId, memberId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReplyLike(
            @PathVariable long postId,
            @PathVariable long replyId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = 1L;
        replyLikeService.unlikeReply(postId, replyId, memberId);

        return ResponseEntity.noContent().build();
    }
}
