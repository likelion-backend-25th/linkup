package net.likelion.bebc25.linkup.subscription.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.subscription.dto.PagingSubListResponse;
import net.likelion.bebc25.linkup.subscription.dto.SubscribeCreatorListResponse;
import net.likelion.bebc25.linkup.subscription.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

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
}
