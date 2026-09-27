package net.likelion.bebc25.linkup.reply.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import net.likelion.bebc25.linkup.reply.dto.ReplyCreateRequest;
import net.likelion.bebc25.linkup.reply.dto.ReplyPageResponse;
import net.likelion.bebc25.linkup.reply.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping
    public ResponseEntity<ReplyPageResponse> getReplies(
            @PathVariable Long postId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = 1L; //userDetails.getId();
        ReplyPageResponse response =
                replyService.getReplies(postId, memberId, cursor, size);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Long> createReply(
            @PathVariable Long postId,
            @RequestBody @Valid ReplyCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = 1L; //userDetails.getId();
        Long replyId = replyService.createReply(postId, memberId, request);

        return ResponseEntity.ok(replyId);
    }
}
