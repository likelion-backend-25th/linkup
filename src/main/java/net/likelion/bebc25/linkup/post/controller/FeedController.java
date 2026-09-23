package net.likelion.bebc25.linkup.post.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.FollowingFeedResponse;
import net.likelion.bebc25.linkup.post.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {
    private final FeedService feedService;

    @GetMapping("/following")
    public ResponseEntity<FollowingFeedResponse> getFollowingFeed(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long memberId = 1L;

        FollowingFeedResponse response =
                feedService.getFollowingFeed(memberId, cursor, size);

        return ResponseEntity.ok(response);
    }

}
