package net.likelion.bebc25.linkup.post.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import net.likelion.bebc25.linkup.post.service.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<Void> insertPostLike(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = 1L;

        postLikeService.likePost(postId, memberId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @PostMapping
    public ResponseEntity<Void> deletePostLike(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = 1L;

        postLikeService.unlikePost(postId, memberId);

        return ResponseEntity.noContent().build();
    }
}
