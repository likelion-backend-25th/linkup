package net.likelion.bebc25.linkup.search.service;

import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;

public interface MemberSearchService {

    // 사용자 검색
    CursorResponse<MemberSearchResponse> searchMembers(
            String keyword,
            SearchFilter filter,
            Long memberId,
            Long cursorId,
            int size
    );

}