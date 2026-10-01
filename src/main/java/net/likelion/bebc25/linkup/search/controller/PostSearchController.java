package net.likelion.bebc25.linkup.search.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;
import net.likelion.bebc25.linkup.search.service.PostSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;

    // 게시글 검색
    @GetMapping("/posts")
    public ResponseEntity<CursorResponse<PostCardResponse>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "ALL") SearchFilter filter,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(value = "X-Member-Id", required = false) Long memberId
    ) {

        return ResponseEntity.ok(
                postSearchService.searchPosts(
                        keyword,
                        filter,
                        memberId,
                        cursorId,
                        size
                )
        );
    }
}
