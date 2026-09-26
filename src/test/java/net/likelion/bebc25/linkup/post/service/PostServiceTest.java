package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.dto.*;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private FileStorageService fileStorageService;

    @Test
    @DisplayName("게시글 등록 및 조회 테스트")
    void createPostByUser() throws IOException {
        // given
        Long memberId = 2L;
        PostCreateRequest request =
                new PostCreateRequest("일반 사용자 게시글", false);

        List<MultipartFile> images = List.of(
                createImage("image1.png"),
                createImage("image2.png")
        );

        // when
        // 1. 첨부 파일이 없으면 등록 성공
        PostCreateResponse postCreateResponse = postService.createPost(memberId, request, images, null);

        // 2. 첨부 파일이 있으면 등록 거절
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                "file".getBytes()
        );
        // 3. 등록한 게시글 단 건 조회
        PostDetailResponse postDetailResponse = postService.getPostDetailById(postCreateResponse.id());

        // then
        // 1. 첨부 파일이 없으면 등록 성공
        assertThat(postCreateResponse).isNotNull();
        // 2. 첨부 파일이 있으면 등록 거절
        assertThatThrownBy(() -> postService.createPost(memberId, request, images, file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("크리에이터만 첨부 파일을 업로드할 수 있습니다.");

        assertThat(postDetailResponse).isNotNull();
        assertThat(postDetailResponse.id()).isEqualTo(postCreateResponse.id());
        assertThat(postDetailResponse.content()).isEqualTo("일반 사용자 게시글");
        assertThat(postDetailResponse.images().get(0).imageOrder()).isEqualTo(1);
        assertThat(postDetailResponse.images().get(1).imageOrder()).isEqualTo(2);
        assertThat(postDetailResponse.images().get(0).imageUrl()).startsWith("/uploads/posts/images/");
        assertThat(postDetailResponse.images().get(1).imageUrl()).startsWith("/uploads/posts/images/");

        for (PostImageResponse image : postDetailResponse.images()) {
            fileStorageService.delete(image.imageUrl());
        }
    }

    @Test
    @DisplayName("작성자의 게시글 삭제 테스트")
    void deletePostByAuthor() {
        // given
        Post post = Post.builder()
                .memberId(1L)
                .content("삭제 테스트")
                .build();

        postMapper.insert(post);
        Long postId = post.getId();

        // 작성자가 삭제하는 경우
        // when
        postService.deletePost(1L, postId);

        // then
        assertThat(postMapper.findById(postId)).isNull();
    }

    @Test
    @DisplayName("작성자가 아닌 회원의 게시글 삭제 테스트")
    void deletePostByOtherMember() {
        // given
        Post post = Post.builder()
                .memberId(1L)
                .content("삭제 테스트")
                .build();

        postMapper.insert(post);
        Long postId = post.getId();

        // when & then
        // 작성자가 아닌 회원이 삭제하는 경우
        assertThatThrownBy(() ->
                postService.deletePost(2L, postId)
        )
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.FORBIDDEN)
                );
        assertThat(postMapper.findById(postId)).isNotNull();
    }

    @Test
    @DisplayName("게시글 본문만 수정 테스트")
    void updateContentTest() {
        // given
        Post post = Post.builder()
                .memberId(1L)
                .content("수정 테스트")
                .fileUrl("test.txt")
                .subscriberOnly(true)
                .build();
        postMapper.insert(post);
        Long postId = post.getId();

        // when
        PostUpdateRequest request = new PostUpdateRequest("수정된 내용", true, false);
        postService.updatePost(postId, 1L, request, null);

        // then
        Post updatedPost = postMapper.findById(postId);
        assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");
        assertThat(updatedPost.getFileUrl()).isEqualTo("test.txt");
        assertThat(updatedPost.isSubscriberOnly()).isTrue();
    }

    @Test
    @DisplayName("게시글 파일도 수정 테스트")
    void updateFileTest() throws IOException {
        // given
        PostCreateRequest createRequest = new PostCreateRequest("수정 테스트", true);
        PostCreateResponse createResponse = postService.createPost(1L, createRequest, List.of(createImage("image1.png")), createFile("test.pdf"));
        Long postId = createResponse.id();
        String oldFileUrl = postMapper.findById(postId).getFileUrl();

        // when
        PostUpdateRequest updateRequest = new PostUpdateRequest("수정된 내용", true, false);
        postService.updatePost(postId, 1L, updateRequest, createFile("updated.pdf"));

        // then
        Post updatedPost = postMapper.findById(postId);
        assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");
        assertThat(updatedPost.getFileUrl())
                .isNotEqualTo(oldFileUrl)
                        .startsWith("/uploads/posts/files/")
                                .endsWith(".pdf");
        assertThat(updatedPost.isSubscriberOnly()).isTrue();

        fileStorageService.delete(updatedPost.getFileUrl());
        fileStorageService.delete(oldFileUrl);
    }

    private MockMultipartFile createImage(String fileName) throws IOException {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);

        return new MockMultipartFile("images", fileName, "image/png", output.toByteArray());
    }

    private MockMultipartFile createFile(String fileName) {
        return new MockMultipartFile(
                "file",
                fileName,
                "application/pdf",
                "test file content".getBytes(StandardCharsets.UTF_8)
        );
    }
}