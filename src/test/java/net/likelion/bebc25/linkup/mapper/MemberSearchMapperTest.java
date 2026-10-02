package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.search.dto.MemberSearchResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;
import net.likelion.bebc25.linkup.search.mapper.MemberSearchMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberSearchMapperTest {

    @Autowired
    private MemberSearchMapper membersearchMapper;

    // 사용자 검색 Test
    @Test
    @DisplayName("닉네임으로 회원을 검색한다")
    void searchMembersByName() {

        List<MemberSearchResponse> result =
                membersearchMapper.searchMembers(
                        "김민준",
                        SearchFilter.ALL,
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
                membersearchMapper.searchMembers(
                        "minjun.frames",
                        SearchFilter.ALL,
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
                membersearchMapper.searchMembers(
                        "문시우",
                        SearchFilter.ALL,
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

        Long memberId = 1L;

        List<MemberSearchResponse> result =
                membersearchMapper.searchMembers(
                        "n",
                        SearchFilter.FOLLOWING,
                        memberId,
                        null,
                        20
                );

        assertThat(result)
                .extracting(MemberSearchResponse::id)
                .containsExactlyInAnyOrder(
                        80L, 69L, 67L, 56L, 54L, 15L, 5L, 4L, 3L, 2L
                );
    }

    @Test
    @DisplayName("내가 구독한 사용자만 검색한다")
    void searchSubscribingMembers() {

        Long memberId = 2L;

        List<MemberSearchResponse> result =
                membersearchMapper.searchMembers(
                        "우",
                        SearchFilter.SUBSCRIBING,
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
                membersearchMapper.searchMembers(
                        "",
                        SearchFilter.ALL,
                        null,
                        20L,
                        20
                );

        assertThat(result)
                .isNotEmpty()
                .allMatch(member -> member.id() < 20L);
    }

}
