package net.likelion.bebc25.linkup.follow.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.follow.dto.FollowMemberPageResponse;
import net.likelion.bebc25.linkup.follow.dto.FollowResponse;
import net.likelion.bebc25.linkup.follow.service.FollowService;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/{targetId}/follow")
    public ResponseEntity<Void> addFollow(
            @PathVariable Long targetId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long memberId = userDetails.getId();

        followService.addFollow(memberId, targetId);

        return ResponseEntity.status(201).build();
    }

    // 언팔로우
    @DeleteMapping("/{targetId}/follow")
    public ResponseEntity<Void> deleteFollow(
            @PathVariable Long targetId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Long memberId = userDetails.getId();

        followService.deleteFollow(memberId, targetId);

        return ResponseEntity.noContent().build();
    }

    // 팔로우 상태 조회
    @GetMapping("/{targetId}/follow")
    public ResponseEntity<FollowResponse> getFollowStatus(
            @PathVariable Long targetId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();

        return ResponseEntity.ok(
                followService.getFollowStatus(memberId, targetId)
        );
    }

    @GetMapping("/{memberId}/followers")
    public ResponseEntity<FollowMemberPageResponse> getFollowers(
            @PathVariable Long memberId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                followService.getFollowers(memberId, cursor, size)
        );
    }

    @GetMapping("/{memberId}/followings")
    public ResponseEntity<FollowMemberPageResponse> getFollowings(
            @PathVariable Long memberId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                followService.getFollowings(memberId, cursor, size)
        );
    }

}
