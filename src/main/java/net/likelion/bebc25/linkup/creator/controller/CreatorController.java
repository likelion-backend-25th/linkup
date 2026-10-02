package net.likelion.bebc25.linkup.creator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.creator.dto.PagingSubscriberListResponse;
import net.likelion.bebc25.linkup.creator.service.CreatorService;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/creators")
public class CreatorController {
    private final CreatorService creatorService;

    @Operation(
            summary = "크리에이터인 사용자의 구독자 리스트 조회",
            description = "크리에이터인 사용자의 Auth 정보에서 ID값을 가져와 구독자 리스트를 조회하여 반환한다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 조회 성공")
    })
    @GetMapping
    public ResponseEntity<PagingSubscriberListResponse> getSubscribeCreatorList(
            @Parameter(description = "로그인 회원")
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "다음 페이징 커서 값", example = "1")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "페이지 사이즈", example = "10")
            @RequestParam(defaultValue = "10") @Min(3) @Max(20) int size
    ) {
        PagingSubscriberListResponse responses
                = creatorService.getSubscriberList(userDetails.getId(), cursor, size);
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "사용자 크리에이터 승인",
            description = "사용자의 크리에이터 신청을 승인한다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "크리에이터 승인 완료")
    })
    @PostMapping
    public ResponseEntity<Void> applyCreator(
            @Parameter(description = "로그인 회원")
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        creatorService.applyCreator(userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
