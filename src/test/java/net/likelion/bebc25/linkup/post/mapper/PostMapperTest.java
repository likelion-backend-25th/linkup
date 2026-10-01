package net.likelion.bebc25.linkup.post.mapper;

import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.mapper.MemberMapper;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.dto.PostDetailRow;
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

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("게시글 상세 조회 테스트")
    public void getPostDetailTest() {
        // given
        Post post = createPost(1L, "테스트 게시글", false);
        Member member = memberMapper.findById(post.getMemberId());

        // when
        PostDetailRow row = postMapper.findDetailById(
                post.getId(),
                member.getId()
        );

        // then
        assertThat(row.getId()).isEqualTo(post.getId());
        assertThat(row.getContent()).isEqualTo(post.getContent());
        assertThat(row.getMemberId()).isEqualTo(post.getMemberId());
        assertThat(row.getName()).isEqualTo(member.getName());
        assertThat(row.getUniqueId()).isEqualTo(member.getUniqueId());
    }

    @Test
    @DisplayName("신규 게시글 등록 테스트")
    public void createPostTest() {
        // given
        Post post = createPost(1L, "테스트 게시글", false);

        // when
        int result = postMapper.insert(post);
        Post postResult = postMapper.findById(
                post.getId(),
                post.getMemberId()
        );

        // then
        assertThat(result).isEqualTo(1);
        assertThat(post.getId()).isNotNull();
        assertThat(postResult.getMemberId()).isEqualTo(1L);
        assertThat(postResult.getContent()).isEqualTo("테스트 게시글");
        assertThat(postResult.isSubscriberOnly()).isEqualTo(false);
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
        assertThat(postMapper.findById(
                postId,
                post.getMemberId()
        )).isNull();
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
        Post postDetail = postMapper.findById(
                postId,
                post.getMemberId()
        );

        assertThat(updateCount).isEqualTo(1);
        assertThat(postDetail.getId()).isEqualTo(updatePost.getId());
        assertThat(postDetail.getContent()).isEqualTo(updatePost.getContent());
        assertThat(postDetail.isSubscriberOnly()).isEqualTo(updatePost.isSubscriberOnly());
        assertThat(postDetail.getFileUrl()).isEqualTo(updatePost.getFileUrl());
    }

    private Post createPost(Long memberId, String content, boolean subscriberOnly) {
        Post post = Post.builder()
                .memberId(memberId)
                .content(content)
                .subscriberOnly(subscriberOnly)
                .build();

        postMapper.insert(post);
        return post;
    }
}