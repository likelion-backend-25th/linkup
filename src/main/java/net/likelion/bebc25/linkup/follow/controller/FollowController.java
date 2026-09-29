package net.likelion.bebc25.linkup.follow.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.follow.dto.FollowResponse;
import net.likelion.bebc25.linkup.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/{targetId}/follow")
    public ResponseEntity<Void> addFollow(
            @PathVariable Long targetId
    ) {

        Long memberId = 2L; // 임시

        followService.addFollow(memberId, targetId);

        return ResponseEntity.status(201).build();
    }

    // 언팔로우
    @DeleteMapping("/{targetId}/follow")
    public ResponseEntity<Void> deleteFollow(
            @PathVariable Long targetId
    ) {

        Long memberId = 2L; // 임시

        followService.deleteFollow(memberId, targetId);

        return ResponseEntity.noContent().build();
    }

    // 팔로우 상태 조회
    @GetMapping("/{targetId}/follow")
    public ResponseEntity<FollowResponse> getFollowStatus(
            @PathVariable Long targetId
    ) {
        Long memberId = 2L; // 임시

        return ResponseEntity.ok(
                followService.getFollowStatus(memberId, targetId)
        );
    }

}