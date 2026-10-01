package net.likelion.bebc25.linkup.search.service;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;

public interface PostSearchService {

    // 게시글 검색
    CursorResponse<PostCardResponse> searchPosts(
            String keyword,
            SearchFilter filter,
            Long memberId,
            Long cursorId,
            int size
    );
}