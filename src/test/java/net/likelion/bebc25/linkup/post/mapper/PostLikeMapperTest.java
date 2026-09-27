package net.likelion.bebc25.linkup.post.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostLikeMapperTest {
    @Autowired
    PostLikeMapper postLikeMapper;

    @Test
    @DisplayName("게시글 좋아요 등록 테스트")
    void insertPostLikeTest() {
        // given

        // when
        int result = postLikeMapper.insert(1L, 2L);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(postLikeMapper.existsByPostIdAndMemberId(1L, 2L)).isTrue();
    }

    @Test
    @DisplayName("게시글 좋아요 삭제 테스트")
    void deletePostLikeTest() {
        // given

        // when
        int result = postLikeMapper.delete(1L, 1L);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(postLikeMapper.existsByPostIdAndMemberId(1L, 1L)).isFalse();
    }
}
