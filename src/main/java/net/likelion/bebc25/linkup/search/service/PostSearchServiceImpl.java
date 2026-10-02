package net.likelion.bebc25.linkup.search.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;
import net.likelion.bebc25.linkup.search.mapper.PostSearchMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;

    @Override
    public CursorResponse<PostCardResponse> searchPosts(
            String keyword,
            SearchFilter filter,
            Long memberId,
            Long cursorId,
            int size
    ) {

        // 검색어 검사
        if (keyword == null || keyword.isBlank()) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "검색어는 필수입니다."
            );
        }

        // 앞뒤 공백 제거
        keyword = keyword.trim();

        // memberId 검사
        if (memberId != null && memberId <= 0) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "memberId는 1 이상이어야 합니다."
            );
        }

        // cursor 검사
        if (cursorId != null && cursorId <= 0) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "cursorId는 1 이상이어야 합니다."
            );
        }

        // size 검사
        if (size <= 0 || size > 100) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "size는 1 이상 100 이하이어야 합니다."
            );
        }

        // FOLLOWING / SUBSCRIBING은 로그인 필요
        if (filter != SearchFilter.ALL && memberId == null) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "팔로우/구독 게시글 검색은 로그인이 필요합니다."
            );
        }

        // 다음 데이터가 있는지 확인하기 위해 size + 1개 조회
        List<PostCardResponse> result =
                postSearchMapper.searchPosts(
                        keyword,
                        filter,
                        memberId,
                        cursorId,
                        size + 1
                );

        // size + 1개가 조회되었다면 다음 데이터가 존재함
        boolean hasNext = result.size() > size;

        // 실제 응답에는 size개만 반환
        List<PostCardResponse> content =
                hasNext
                        ? result.subList(0, size)
                        : result;

        // 다음 cursor
        Long nextCursor = hasNext
                ? content.get(content.size() - 1).postId()
                : null;

        return CursorResponse.of(
                content,
                nextCursor,
                hasNext
        );
    }

}