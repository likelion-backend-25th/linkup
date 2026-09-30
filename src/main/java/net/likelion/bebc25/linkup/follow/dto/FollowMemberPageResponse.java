package net.likelion.bebc25.linkup.follow.dto;

import java.util.List;

public record FollowMemberPageResponse(
        List<FollowMemberResponse> content,
        Long nextCursor,
        boolean hasNext
) {
}
