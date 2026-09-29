package net.likelion.bebc25.linkup.search.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.UserSearchFilter;
import net.likelion.bebc25.linkup.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/members")
    public ResponseEntity<CursorResponse<MemberSearchResponse>> searchMembers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "ALL") UserSearchFilter filter,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(value = "X-Member-Id", required = false) Long memberId
    ) {

        return ResponseEntity.ok(
                searchService.searchMembers(
                        keyword,
                        filter,
                        memberId,
                        cursorId,
                        size
                )
        );
    }
}
