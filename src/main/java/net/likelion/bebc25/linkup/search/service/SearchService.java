package net.likelion.bebc25.linkup.search.service;

import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.UserSearchFilter;

public interface SearchService {

    CursorResponse<MemberSearchResponse> searchMembers(
            String keyword,
            UserSearchFilter filter,
            Long memberId,
            Long cursorId,
            int size
    );
}