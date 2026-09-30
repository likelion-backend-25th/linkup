package net.likelion.bebc25.linkup.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import net.likelion.bebc25.linkup.post.dto.FeedResponse;
import net.likelion.bebc25.linkup.post.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FeedController {
    private final FeedService feedService;

    @Operation(summary = "내가 팔로우한 회원의 피드 조회", description = "내가 팔로우한 회원의 피드를 무한 스크롤 형식으로 제공합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로잉 피드 조회 성공")
    })
    @GetMapping("/feeds/following")
    public ResponseEntity<FeedResponse> getFollowingFeed(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();

        FeedResponse response =
                feedService.getFollowingFeed(memberId, cursor, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 구독한 회원의 피드 조회", description = "내가 구독한 회원의 피드를 무한 스크롤 형식으로 제공합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 피드 조회 성공")
    })
    @GetMapping("/feeds/subscription")
    public ResponseEntity<FeedResponse> getSubscriptionFeed(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();

        FeedResponse response =
                feedService.getSubscriptionFeed(memberId, cursor, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 좋아요 순 조회", description = "전체 공개 게시글을 좋아요 순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 회원 구독자 전용 피드 조회 성공"),
    })
    @GetMapping("/feeds/popular")
    public ResponseEntity<FeedResponse> getPopularFeed(
            @RequestParam(required = false) Integer cursorLikeCount,
            @RequestParam(required = false) Long cursorPostId,
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size
    ) {
        FeedResponse response =
                feedService.getPopularFeed(cursorLikeCount, cursorPostId, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 전체 공개 게시글 조회", description = "내가 등록한 전체 공개 게시글 피드를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 전체 공개 피드 조회 성공")
    })
    @GetMapping("/members/me/feeds")
    public ResponseEntity<FeedResponse> getMyFeed(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();

        FeedResponse response =
                feedService.getMyFeed(memberId, cursor, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 구독자 전용 게시글 조회", description = "내가 등록한 구독자 전용 게시글 피드를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 구독자 전용 피드 조회 성공")
    })
    @GetMapping("/members/me/feeds/subscriber-only")
    public ResponseEntity<FeedResponse> getMySubscriberOnlyFeed(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();

        FeedResponse response =
                feedService.getMySubscriberOnlyFeed(memberId, cursor, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 회원의 전체 공개 게시글 조회", description = "특정 회원이 등록한 전체 공개 게시글 피드를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 회원 전체 공개 피드 조회 성공")
    })
    @GetMapping("/members/{targetId}/feeds")
    public ResponseEntity<FeedResponse> getTargetFeed(
            @PathVariable Long targetId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size
    ) {
        FeedResponse response =
                feedService.getTargetFeed(targetId, cursor, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 회원의 구독자 전용 게시글 조회", description = "특정 회원이 등록한 구독자 전용 게시글 피드를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 회원 구독자 전용 피드 조회 성공"),
            @ApiResponse(responseCode = "403", description = "게시글에 접근할 수 없음", content = @Content)
    })
    @GetMapping("/members/{targetId}/feeds/subscriber-only")
    public ResponseEntity<FeedResponse> getTargetSubscriberOnlyFeed(
            @PathVariable Long targetId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();

        FeedResponse response =
                feedService.getTargetSubscriberOnlyFeed(memberId, targetId, cursor, size);

        return ResponseEntity.ok(response);
    }
}
