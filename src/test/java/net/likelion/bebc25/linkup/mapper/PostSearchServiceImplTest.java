package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;
import net.likelion.bebc25.linkup.search.mapper.PostSearchMapper;
import net.likelion.bebc25.linkup.search.service.PostSearchServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class PostSearchServiceImplTest {

    @InjectMocks
    private PostSearchServiceImpl postSearchService;

    @Mock
    private PostSearchMapper postSearchMapper;

    // 게시글 검색

    // 정상 동작 테스트

    @Nested
    @DisplayName("게시글 검색 정상 동작 테스트")
    class PostSearchSuccessTest {

        @Test
        @DisplayName("올바른 조건으로 검색하면 게시글 목록을 반환한다")
        void searchPostsSuccess() {

            // given
            String keyword = "스프링";
            SearchFilter filter = SearchFilter.ALL;
            Long memberId = null;
            Long cursorId = null;
            int size = 20;

            List<PostCardResponse> mockContent = List.of(
                    new PostCardResponse(
                            1L,
                            1L,
                            "김민준",
                            "minjun.frames",
                            "profile.png",
                            "스프링 공부를 시작했습니다.",
                            "https://cdn.example.com/posts/1/image-1.jpg",
                            10,
                            3,
                            false,
                            false,
                            LocalDateTime.now()
                    )
            );

            given(postSearchMapper.searchPosts(
                    keyword,
                    filter,
                    memberId,
                    cursorId,
                    size + 1
            )).willReturn(mockContent);

            // when
            CursorResponse<PostCardResponse> response =
                    postSearchService.searchPosts(
                            keyword,
                            filter,
                            memberId,
                            cursorId,
                            size
                    );

            // then
            assertThat(response).isNotNull();

            assertThat(response.content())
                    .hasSize(1);

            assertThat(response.content().get(0).postId())
                    .isEqualTo(1L);

            assertThat(response.content().get(0).content())
                    .isEqualTo("스프링 공부를 시작했습니다.");

            assertThat(response.content().get(0).mainImageUrl())
                    .isEqualTo(
                            "https://cdn.example.com/posts/1/image-1.jpg"
                    );

            assertThat(response.hasNext())
                    .isFalse();

            assertThat(response.nextCursor())
                    .isNull();
        }

        @Test
        @DisplayName("게시글 검색어의 앞뒤 공백을 제거한 후 검색한다")
        void trimPostKeyword() {

            // given
            given(postSearchMapper.searchPosts(
                    "스프링",
                    SearchFilter.ALL,
                    null,
                    null,
                    21
            )).willReturn(List.of());

            // when
            postSearchService.searchPosts(
                    "  스프링  ",
                    SearchFilter.ALL,
                    null,
                    null,
                    20
            );

            // then
            verify(postSearchMapper).searchPosts(
                    "스프링",
                    SearchFilter.ALL,
                    null,
                    null,
                    21
            );
        }

        @Test
        @DisplayName("다음 게시글이 있으면 nextCursor와 hasNext를 반환한다")
        void searchPostsHasNext() {

            // given
            String keyword = "스프링";
            SearchFilter filter = SearchFilter.ALL;
            Long memberId = null;
            Long cursorId = null;
            int size = 20;

            List<PostCardResponse> mockContent =
                    LongStream.rangeClosed(1, 21)
                            .mapToObj(id ->
                                    new PostCardResponse(
                                            id,
                                            1L,
                                            "회원" + id,
                                            "user" + id,
                                            "profile.png",
                                            "스프링 게시글 " + id,
                                            "https://cdn.example.com/posts/"
                                                    + id
                                                    + "/image.jpg",
                                            10,
                                            3,
                                            false,
                                            false,
                                            LocalDateTime.now()
                                    )
                            )
                            .toList();

            given(postSearchMapper.searchPosts(
                    keyword,
                    filter,
                    memberId,
                    cursorId,
                    size + 1
            )).willReturn(mockContent);

            // when
            CursorResponse<PostCardResponse> response =
                    postSearchService.searchPosts(
                            keyword,
                            filter,
                            memberId,
                            cursorId,
                            size
                    );

            // then
            assertThat(response.content())
                    .hasSize(20);

            assertThat(response.hasNext())
                    .isTrue();

            assertThat(response.nextCursor())
                    .isEqualTo(20L);
        }

        @Test
        @DisplayName("커서 ID를 사용하여 다음 게시글 목록을 조회한다")
        void searchPostsWithCursor() {

            // given
            String keyword = "스프링";
            SearchFilter filter = SearchFilter.ALL;
            Long memberId = null;
            Long cursorId = 20L;
            int size = 20;

            List<PostCardResponse> mockContent = List.of(
                    new PostCardResponse(
                            19L,
                            1L,
                            "김민준",
                            "minjun.frames",
                            "profile.png",
                            "스프링 다음 공부",
                            "https://cdn.example.com/posts/19/image.jpg",
                            5,
                            2,
                            false,
                            false,
                            LocalDateTime.now()
                    )
            );

            given(postSearchMapper.searchPosts(
                    keyword,
                    filter,
                    memberId,
                    cursorId,
                    size + 1
            )).willReturn(mockContent);

            // when
            CursorResponse<PostCardResponse> response =
                    postSearchService.searchPosts(
                            keyword,
                            filter,
                            memberId,
                            cursorId,
                            size
                    );

            // then
            assertThat(response.content())
                    .hasSize(1);

            assertThat(response.content().get(0).postId())
                    .isEqualTo(19L);

            assertThat(response.hasNext())
                    .isFalse();

            assertThat(response.nextCursor())
                    .isNull();
        }
    }

    // 게시글 검색 Validation

    @Nested
    @DisplayName("게시글 검색 예외 발생 테스트")
    class PostSearchValidationTest {

        @Test
        @DisplayName("게시글 검색어가 비어있으면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenPostKeywordIsBlank() {

            assertThatThrownBy(() ->
                    postSearchService.searchPosts(
                            "",
                            SearchFilter.ALL,
                            null,
                            null,
                            20
                    )
            )
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue(
                            "status",
                            BAD_REQUEST
                    )
                    .hasMessageContaining(
                            "검색어는 필수입니다."
                    );
        }

        @Test
        @DisplayName("게시글 검색의 cursorId가 0 이하이면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenPostCursorIdIsInvalid() {

            // cursorId가 0일 때
            assertThatThrownBy(() ->
                    postSearchService.searchPosts(
                            "스프링",
                            SearchFilter.ALL,
                            null,
                            0L,
                            20
                    )
            )
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue(
                            "status",
                            BAD_REQUEST
                    )
                    .hasMessageContaining(
                            "cursorId는 1 이상이어야 합니다."
                    );

            // cursorId가 음수일 때
            assertThatThrownBy(() ->
                    postSearchService.searchPosts(
                            "스프링",
                            SearchFilter.ALL,
                            null,
                            -1L,
                            20
                    )
            )
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue(
                            "status",
                            BAD_REQUEST
                    )
                    .hasMessageContaining(
                            "cursorId는 1 이상이어야 합니다."
                    );
        }

        @Test
        @DisplayName("게시글 검색의 size가 1 미만이거나 100을 초과하면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenPostSizeIsInvalid() {

            // size가 0일 때
            assertThatThrownBy(() ->
                    postSearchService.searchPosts(
                            "스프링",
                            SearchFilter.ALL,
                            null,
                            null,
                            0
                    )
            )
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue(
                            "status",
                            BAD_REQUEST
                    )
                    .hasMessageContaining(
                            "size는 1 이상 100 이하이어야 합니다."
                    );

            // size가 101일 때
            assertThatThrownBy(() ->
                    postSearchService.searchPosts(
                            "스프링",
                            SearchFilter.ALL,
                            null,
                            null,
                            101
                    )
            )
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue(
                            "status",
                            BAD_REQUEST
                    )
                    .hasMessageContaining(
                            "size는 1 이상 100 이하이어야 합니다."
                    );
        }

        @Test
        @DisplayName("로그인하지 않고 팔로우/구독 게시글 필터를 설정하면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenPostFilterRequiresLogin() {

            assertThatThrownBy(() ->
                    postSearchService.searchPosts(
                            "스프링",
                            SearchFilter.FOLLOWING,
                            null,
                            null,
                            20
                    )
            )
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue(
                            "status",
                            BAD_REQUEST
                    )
                    .hasMessageContaining(
                            "팔로우/구독 게시글 검색은 로그인이 필요합니다."
                    );

            assertThatThrownBy(() ->
                    postSearchService.searchPosts(
                            "스프링",
                            SearchFilter.SUBSCRIBING,
                            null,
                            null,
                            20
                    )
            )
                    .isInstanceOf(ResponseStatusException.class)
                    .hasFieldOrPropertyWithValue(
                            "status",
                            BAD_REQUEST
                    )
                    .hasMessageContaining(
                            "팔로우/구독 게시글 검색은 로그인이 필요합니다."
                    );
        }
    }

}
