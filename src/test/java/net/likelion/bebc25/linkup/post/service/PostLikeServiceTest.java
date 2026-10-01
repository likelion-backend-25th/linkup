package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.mapper.PostLikeMapper;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PostLikeServiceTest {

    @Autowired
    PostLikeService postLikeService;

    @Autowired
    PostMapper postMapper;

    @Autowired
    PostLikeMapper postLikeMapper;

    @Test
    @DisplayName("게시글 좋아요 등록 테스트 | 1. 공개된 게시글 좋아요")
    void insertPostLikeTest1() {
        // given
        Post post = createPost(1L, "테스트 게시글", false);

        // when
        postLikeService.likePost(post.getId(), 2L);
        Post resultPost = postMapper.findById(post.getId(), 2L);

        // then
        assertThat(resultPost.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 좋아요 등록 테스트 | 2. 구독자 전용 게시글 좋아요(구독자 아닌 경우)")
    void insertPostLikeTest2() {
        // given
        Post post = createPost(5L, "테스트 게시글", true);

        // when
        assertThatThrownBy(() ->
                postLikeService.likePost(post.getId(), 1L)
        )
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        AssertionsForClassTypes.assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.FORBIDDEN)
                );

        Post resultPost = postMapper.findById(post.getId(), 1L);

        // then
        assertThat(resultPost.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 좋아요 등록 테스트 | 3. 구독자 전용 게시글 좋아요(구독자인 경우)")
    void insertPostLikeTest3() {
        // given
        Post post = createPost(5L, "테스트 게시글", false);

        // when
        postLikeService.likePost(post.getId(), 2L);
        Post resultPost = postMapper.findById(post.getId(), 2L);

        // then
        assertThat(resultPost.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 좋아요 삭제 테스트 | 1. 공개된 게시글 좋아요")
    void deletePostLikeTest1() {
        // given
        Post post = createPost(1L, "테스트 게시글", false);

        // when
        postLikeService.likePost(post.getId(), 2L);
        postLikeService.likePost(post.getId(), 3L);
        postLikeService.unlikePost(post.getId(), 2L);

        Post resultPost = postMapper.findById(post.getId(), 2L);

        // then
        assertThat(resultPost.getLikeCount()).isEqualTo(1);
        assertThat(postLikeMapper.existsByPostIdAndMemberId(post.getId(), 2L)).isFalse();
    }

    Post createPost(Long memberId, String content, boolean subscriberOnly) {
        Post post = Post.builder()
                .memberId(memberId)
                .content(content)
                .subscriberOnly(subscriberOnly)
                .build();

        postMapper.insert(post);

        return post;
    }
}