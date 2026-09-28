package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.CursorResponse;
import net.likelion.bebc25.linkup.search.dto.UserSearchFilter;
import net.likelion.bebc25.linkup.search.mapper.SearchMapper;
import net.likelion.bebc25.linkup.search.service.SearchServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private SearchMapper searchMapper;


    // =========================================================
    // 정상 동작 테스트
    // =========================================================

    @Nested
    @DisplayName("정상 동작 테스트")
    class SuccessTest {

        @Test
        @DisplayName("올바른 조건으로 검색하면 회원 목록을 반환한다")
        void searchMembersSuccess() {

            // given
            String keyword = "  김민준  ";
            String trimmedKeyword = "김민준";

            UserSearchFilter filter = UserSearchFilter.ALL;
            Long memberId = null;

            Long cursorId = null;
            int size = 20;

            List<MemberSearchResponse> mockContent = List.of(
                    new MemberSearchResponse(
                            1L,
                            "김민준",
                            "minjun.frames",
                            "profile.png"
                    )
            );

            given(searchMapper.searchMembers(
                    trimmedKeyword,
                    filter,
                    memberId,
                    cursorId,
                    size + 1
            )).willReturn(mockContent);

            // when
            CursorResponse<MemberSearchResponse> response =
                    searchService.searchMembers(
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

            assertThat(response.content().get(0).name())
                    .isEqualTo("김민준");

            assertThat(response.nextCursor())
                    .isNull();

            assertThat(response.hasNext())
                    .isFalse();
        }


        @Test
        @DisplayName("커서 ID를 사용하여 다음 회원 목록을 조회한다")
        void searchMembersWithCursor() {

            // given
            String keyword = "김";
            UserSearchFilter filter = UserSearchFilter.ALL;
            Long memberId = null;

            Long cursorId = 20L;
            int size = 20;

            List<MemberSearchResponse> mockContent = List.of(
                    new MemberSearchResponse(
                            19L,
                            "김철수",
                            "kim19",
                            "profile.png"
                    )
            );

            given(searchMapper.searchMembers(
                    keyword,
                    filter,
                    memberId,
                    cursorId,
                    size + 1
            )).willReturn(mockContent);

            // when
            CursorResponse<MemberSearchResponse> response =
                    searchService.searchMembers(
                            keyword,
                            filter,
                            memberId,
                            cursorId,
                            size
                    );

            // then
            assertThat(response.content())
                    .hasSize(1);

            assertThat(response.content().get(0).id())
                    .isEqualTo(19L);

            assertThat(response.hasNext())
                    .isFalse();

            assertThat(response.nextCursor())
                    .isNull();
        }


        @Test
        @DisplayName("다음 회원이 있으면 nextCursor와 hasNext를 반환한다")
        void searchMembersHasNext() {

            // given
            String keyword = "김";
            UserSearchFilter filter = UserSearchFilter.ALL;
            Long memberId = null;

            Long cursorId = null;
            int size = 20;

            // 21개 조회
            List<MemberSearchResponse> mockContent =
                    LongStream.rangeClosed(1, 21)
                            .mapToObj(id ->
                                    new MemberSearchResponse(
                                            id,
                                            "회원" + id,
                                            "user" + id,
                                            "profile.png"
                                    )
                            )
                            .toList();

            given(searchMapper.searchMembers(
                    keyword,
                    filter,
                    memberId,
                    cursorId,
                    size + 1
            )).willReturn(mockContent);

            // when
            CursorResponse<MemberSearchResponse> response =
                    searchService.searchMembers(
                            keyword,
                            filter,
                            memberId,
                            cursorId,
                            size
                    );

            // then
            // 실제 응답은 20개만 반환
            assertThat(response.content())
                    .hasSize(20);

            // 다음 데이터가 존재
            assertThat(response.hasNext())
                    .isTrue();

            // 마지막 데이터의 ID가 다음 cursor
            assertThat(response.nextCursor())
                    .isEqualTo(20L);
        }
    }


    // =========================================================
    // Validation 테스트
    // =========================================================

    @Nested
    @DisplayName("예외(Validation) 발생 테스트")
    class ValidationErrorTest {

        @Test
        @DisplayName("검색어가 비어있거나 공백이면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenKeywordIsBlank() {

            // 빈 문자열
            assertThatThrownBy(() ->
                    searchService.searchMembers(
                            "",
                            UserSearchFilter.ALL,
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


            // null
            assertThatThrownBy(() ->
                    searchService.searchMembers(
                            null,
                            UserSearchFilter.ALL,
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
        @DisplayName("cursorId가 0 이하이면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenCursorIdIsInvalid() {

            // cursorId가 0일 때
            assertThatThrownBy(() ->
                    searchService.searchMembers(
                            "김민준",
                            UserSearchFilter.ALL,
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
                    searchService.searchMembers(
                            "김민준",
                            UserSearchFilter.ALL,
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
        @DisplayName("사이즈가 1 미만이거나 100을 초과하면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenSizeIsInvalid() {

            // size가 0일 때
            assertThatThrownBy(() ->
                    searchService.searchMembers(
                            "김민준",
                            UserSearchFilter.ALL,
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
                    searchService.searchMembers(
                            "김민준",
                            UserSearchFilter.ALL,
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
        @DisplayName("로그인하지 않고 팔로우/구독 필터를 설정하면 BAD_REQUEST 예외가 발생한다")
        void throwExceptionWhenFilterRequiresLoginButMemberIdIsNull() {

            // FOLLOWING
            assertThatThrownBy(() ->
                    searchService.searchMembers(
                            "김민준",
                            UserSearchFilter.FOLLOWING,
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
                            "팔로우/구독 사용자 검색은 로그인이 필요합니다."
                    );


            // SUBSCRIBING
            assertThatThrownBy(() ->
                    searchService.searchMembers(
                            "김민준",
                            UserSearchFilter.SUBSCRIBING,
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
                            "팔로우/구독 사용자 검색은 로그인이 필요합니다."
                    );
        }
    }
}
