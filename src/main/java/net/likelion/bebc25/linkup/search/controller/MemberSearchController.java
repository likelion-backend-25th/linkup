package net.likelion.bebc25.linkup.search.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;
import net.likelion.bebc25.linkup.search.service.MemberSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    // 사용자 검색
    @GetMapping("/members")
    public ResponseEntity<CursorResponse<MemberSearchResponse>> searchMembers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "ALL") SearchFilter filter,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(value = "X-Member-Id", required = false) Long memberId
    ) {

        return ResponseEntity.ok(
                memberSearchService.searchMembers(
                        keyword,
                        filter,
                        memberId,
                        cursorId,
                        size
                )
        );
    }
}
