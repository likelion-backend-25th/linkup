package net.likelion.bebc25.linkup.subscription.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.subscription.dto.*;
import net.likelion.bebc25.linkup.subscription.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "구독 등록",
            description = "사용자의 Auth정보와 Body 정보를 기반으로 구독을 등록한다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "구독 리스트 조회 성공")
    })
    @PostMapping
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(
            @Parameter(description = "로그인 회원 ID", example = "1")
            @RequestHeader("X-Member-Id") Long memberId,
            @Parameter(description = "구독 등록 요청 정보")
            @RequestBody CreateSubscriptionRequest createSubscriptionRequest
    ) {
        CreateSubscriptionResponse response
                = subscriptionService.createSubscription(memberId, createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "사용자의 구독 리스트 조회",
            description = "사용자의 Auth 정보에서 ID값을 가져와 구독 리스트를 조회하여 반환한다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 리스트 조회 성공")
    })
    @GetMapping
    public ResponseEntity<PagingSubListResponse> getSubscribeCreatorList(
            @Parameter(description = "로그인 회원 ID", example = "1")
            @RequestHeader("X-Member-Id") Long memberId,
            @Parameter(description = "다음 페이징 커서 값", example = "1")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "페이지 사이즈", example = "10")
            @RequestParam(defaultValue = "9") @Min(3) @Max(20) int size
    ) {
        PagingSubListResponse responses
                = subscriptionService.getSubscribeCreatorList(memberId, cursor, size);
        return ResponseEntity.ok(responses);
    }


    @Operation(
            summary = "구독 내역 상세 조회",
            description = "구독 id 값을 사용하여 구독 내역을 상세 조회 한다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 상세 내역 조회 성공")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDetailResponse> getSubscriptionDetail(
            // 로그인 되야함을 명시, 값은 쓰지 않음
            @Parameter(description = "로그인 회원 ID", example = "1")
            @RequestHeader(value = "X-Member-Id", required = false) Long memberId,
            @Parameter(description = "구독 내역 ID", example = "1")
            @PathVariable("id") Long subscriptionId
    ) {
        SubscriptionDetailResponse response = subscriptionService.getSubscriptionDetail(subscriptionId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "구독 해지",
            description = "구독 id 값을 사용하여 구독을 해지한다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "구독 해지 성공")
    })
    @PostMapping("/{id}")
    public ResponseEntity<Void> cancelSubscription(
            // 로그인 되야함을 명시, 값은 쓰지 않음
            @Parameter(description = "로그인 회원 ID", example = "1")
            @RequestHeader(value = "X-Member-Id", required = false) Long memberId,
            @Parameter(description = "구독 내역 ID", example = "1")
            @PathVariable("id") Long subscriptionId
    ) {
        subscriptionService.cancelSubscription(subscriptionId);
//        SubscriptionDetailResponse response = subscriptionService.getSubscriptionDetail(subscriptionId);
//        return ResponseEntity.ok(response);
        return ResponseEntity.noContent().build();
    }
}
