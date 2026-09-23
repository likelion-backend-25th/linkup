package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.domain.PostImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class PostImageMapperTest {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostImageMapper postImageMapper;

    @Test
    @DisplayName("게시글 이미지를 저장 테스트")
    void insertPostImage() {
        // given
        Post post = Post.builder()
                .memberId(1L) // 테스트 DB에 존재하는 회원
                .content("이미지 저장 테스트")
                .subscriberOnly(false)
                .build();

        postMapper.insert(post);

        PostImage postImage = PostImage.builder()
                .postId(post.getId())
                .imageUrl("/uploads/test-image.png")
                .imageOrder(0)
                .build();

        // when
        postImageMapper.insert(postImage);

        // then
        assertThat(postImage.getId()).isNotNull();
    }
}
