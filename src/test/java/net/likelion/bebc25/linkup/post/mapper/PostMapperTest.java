package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Test
    @DisplayName("신규 게시글 등록")
    public void createPostTest() {
        // given
        Post post = Post.builder()
                .memberId(1L)
                .content("테스트 게시글")
                .subscriberOnly(false)
                .build();

        // when
        int result = postMapper.insert(post);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(post.getId()).isNotNull();
    }
}
