package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.UserSearchFilter;
import net.likelion.bebc25.linkup.search.mapper.SearchMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SearchMapperTest {

    @Autowired
    private SearchMapper searchMapper;

    @Test
    @DisplayName("닉네임으로 회원을 검색한다")
    void searchMembersByName() {

        List<MemberSearchResponse> result =
                searchMapper.searchMembers(
                        "김민준",
                        UserSearchFilter.ALL,
                        null,
                        null,
                        20
                );

        assertThat(result)
                .extracting(MemberSearchResponse::id)
                .containsExactly(1L);
    }

    @Test
    @DisplayName("사용자 아이디로 회원을 검색한다")
    void searchMembersByUniqueId() {

        List<MemberSearchResponse> result =
                searchMapper.searchMembers(
                        "minjun.frames",
                        UserSearchFilter.ALL,
                        null,
                        null,
                        20
                );

        assertThat(result)
                .extracting(MemberSearchResponse::id)
                .containsExactly(1L);
    }

    @Test
    @DisplayName("로그인 사용자의 차단 관계 회원을 검색에서 제외한다")
    void searchMembersExcludeBlockedUsers() {

        Long memberId = 1L;

        List<MemberSearchResponse> result =
                searchMapper.searchMembers(
                        "",
                        UserSearchFilter.ALL,
                        memberId,
                        null,
                        20
                );

        assertThat(result)
                .extracting(MemberSearchResponse::id)
                .doesNotContain(21L);
    }

    @Test
    @DisplayName("내가 팔로우한 사용자만 검색한다")
    void searchFollowingMembers() {

        Long memberId = 10L;

        List<MemberSearchResponse> result =
                searchMapper.searchMembers(
                        "",
                        UserSearchFilter.FOLLOWING,
                        memberId,
                        null,
                        20
                );

        assertThat(result)
                .extracting(MemberSearchResponse::id)
                .containsExactlyInAnyOrder(
                        11L, 12L, 13L, 14L, 62L, 75L
                );
    }

    @Test
    @DisplayName("내가 구독한 사용자만 검색한다")
    void searchSubscribingMembers() {

        Long memberId = 2L;

        List<MemberSearchResponse> result =
                searchMapper.searchMembers(
                        "",
                        UserSearchFilter.SUBSCRIBING,
                        memberId,
                        null,
                        20
                );

        assertThat(result)
                .extracting(MemberSearchResponse::id)
                .containsExactly(5L);
    }

    @Test
    @DisplayName("커서 ID보다 작은 회원을 조회한다")
    void searchMembersWithCursor() {

        List<MemberSearchResponse> result =
                searchMapper.searchMembers(
                        "",
                        UserSearchFilter.ALL,
                        null,
                        20L,
                        20
                );

        assertThat(result)
                .allMatch(member -> member.id() < 20L);
    }
}
