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
    @DisplayName("신규 게시글 등록 및 조회 테스트")
    public void createPostTest() {
        // given
        Post post = Post.builder()
                .memberId(1L)
                .content("테스트 게시글")
                .subscriberOnly(false)
                .build();

        // when
        int result = postMapper.insert(post);
        Post postDetail = postMapper.findById(post.getId());

        // then
        assertThat(result).isEqualTo(1);
        assertThat(post.getId()).isNotNull();
        assertThat(postDetail.getMemberId()).isEqualTo(1L);
        assertThat(postDetail.getContent()).isEqualTo("테스트 게시글");
        assertThat(postDetail.isSubscriberOnly()).isEqualTo(false);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deletePostByIdTest() {
        // given
        Post post = Post.builder()
                .memberId(1L)
                .content("삭제 테스트")
                .fileUrl(null)
                .subscriberOnly(false)
                .build();
        postMapper.insert(post);
        Long postId = post.getId();

        // when
        int deletedCount = postMapper.deleteById(postId);

        // then
        assertThat(deletedCount).isEqualTo(1);
        assertThat(postMapper.findById(postId)).isNull();
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void updatePostByIdTest() {
        // given
        Post post = Post.builder()
                .memberId(1L)
                .content("수정 테스트")
                .fileUrl(null)
                .subscriberOnly(false)
                .build();
        postMapper.insert(post);
        Long postId = post.getId();

        Post updatePost = Post.builder()
                .id(postId)
                .memberId(1L)
                .content("수정된 내용")
                .fileUrl("test.txt")
                .subscriberOnly(true)
                .build();
        // when
        int updateCount = postMapper.updateById(updatePost);

        // then
        Post postDetail = postMapper.findById(postId);
        assertThat(updateCount).isEqualTo(1);
        assertThat(postDetail.getId()).isEqualTo(updatePost.getId());
        assertThat(postDetail.getContent()).isEqualTo(updatePost.getContent());
        assertThat(postDetail.isSubscriberOnly()).isEqualTo(updatePost.isSubscriberOnly());
        assertThat(postDetail.getFileUrl()).isEqualTo(updatePost.getFileUrl());
    }
}
