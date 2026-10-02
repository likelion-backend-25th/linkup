package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.post.dto.PostCardResponse;
import net.likelion.bebc25.linkup.search.dto.SearchFilter;
import net.likelion.bebc25.linkup.search.mapper.PostSearchMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostSearchMapperTest {

    @Autowired
    private PostSearchMapper postsearchMapper;

    // 게시글 검색 Test
    @Test
    @DisplayName("게시글 내용으로 게시글을 검색한다")
    void searchPostsByContent() {

        List<PostCardResponse> result =
                postsearchMapper.searchPosts(
                        "구름",
                        SearchFilter.ALL,
                        null,
                        null,
                        20
                );

        assertThat(result)
                .extracting(PostCardResponse::postId)
                .contains(1L);
    }

    @Test
    @DisplayName("내가 팔로우한 사용자의 게시글만 검색한다")
    void searchFollowingPosts() {

        Long memberId = 1L;

        List<PostCardResponse> result =
                postsearchMapper.searchPosts(
                        "길",
                        SearchFilter.FOLLOWING,
                        memberId,
                        null,
                        20
                );

        assertThat(result)
                .extracting(PostCardResponse::memberId)
                .containsOnly(2L, 3L, 5L, 15L, 80L);
    }

    @Test
    @DisplayName("차단한 사용자의 게시글을 검색에서 제외한다")
    void searchPostsExcludeBlockedUsers() {

        Long memberId = 1L;

        List<PostCardResponse> result =
                postsearchMapper.searchPosts(
                        "",
                        SearchFilter.ALL,
                        memberId,
                        null,
                        20
                );

        assertThat(result)
                .extracting(PostCardResponse::memberId)
                .doesNotContain(21L);
    }

    @Test
    @DisplayName("비로그인 사용자는 구독자 전용 게시글을 검색할 수 없다")
    void searchPostsExcludeSubscriberOnlyForAnonymous() {

        List<PostCardResponse> result =
                postsearchMapper.searchPosts(
                        "서울숲",
                        SearchFilter.ALL,
                        null,
                        null,
                        20
                );

        assertThat(result)
                .extracting(PostCardResponse::subscriberOnly)
                .doesNotContain(true);
    }

    @Test
    @DisplayName("구독 중이어도 차단한 사용자의 게시글은 검색하지 않는다")
    void searchPostsExcludeBlockedUserEvenIfSubscribed() {

        Long memberId = 1L;

        List<PostCardResponse> result =
                postsearchMapper.searchPosts(
                        "",
                        SearchFilter.SUBSCRIBING,
                        memberId,
                        null,
                        20
                );

        assertThat(result)
                .extracting(PostCardResponse::memberId)
                .doesNotContain(21L);
    }

    @Test
    @DisplayName("커서 ID보다 작은 게시글을 조회한다")
    void searchPostsWithCursor() {

        List<PostCardResponse> result =
                postsearchMapper.searchPosts(
                        "",
                        SearchFilter.ALL,
                        null,
                        20L,
                        20
                );

        assertThat(result)
                .allMatch(post -> post.postId() < 20L);
    }

}
