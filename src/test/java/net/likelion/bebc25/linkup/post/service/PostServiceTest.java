package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.domain.PostImage;
import net.likelion.bebc25.linkup.post.dto.*;
import net.likelion.bebc25.linkup.post.mapper.PostImageMapper;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.assertj.core.api.AssertionsForClassTypes;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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
    @Autowired
    private PostImageMapper postImageMapper;

    @Test
    @DisplayName("게시글 상세 조회 테스트 | 1. 공개 게시글 조회")
    public void getPostDetail1() {
        // given

        // when
        PostDetailResponse postDetailResponse = postService.getPostDetailById(2L, 1L);

        // then
        assertThat(postDetailResponse).isNotNull();
        assertThat(postDetailResponse.id()).isEqualTo(2L);
        assertThat(postDetailResponse.memberId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("게시글 상세 조회 테스트 | 2. 구독자 전용 게시글 조회")
    public void getPostDetail2() {
        // given

        // when
        assertThatThrownBy(() ->
                postService.getPostDetailById(12L, 1L)
        )
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        AssertionsForClassTypes.assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.FORBIDDEN)
                );
    }

    @Test
    @DisplayName("게시글 등록 테스트 | 1. 공개 게시글 등록 (첨부파일 없는 경우)")
    void createPost1() throws IOException {
        // given
        Long memberId = 2L;
        PostCreateRequest request = new PostCreateRequest("테스트 게시글", false);

        List<MultipartFile> images = List.of(
                createImage("image1.png"),
                createImage("image2.png")
        );
        // when
        PostCreateResponse response = postService.createPost(memberId, request, images, null);


        // then: 게시글 저장 확인
        Post savedPost = postMapper.findById(response.id());

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getMemberId()).isEqualTo(memberId);
        assertThat(savedPost.getContent()).isEqualTo("테스트 게시글");
        assertThat(savedPost.isSubscriberOnly()).isFalse();
        assertThat(savedPost.getFileUrl()).isNull();

        // then: 이미지 정보 저장 확인
        List<PostImage> savedImages =
                postImageMapper.findAllByPostId(response.id());

        assertThat(savedImages).hasSize(2);
        assertThat(savedImages)
                .extracting(PostImage::getImageOrder)
                .containsExactly(1, 2);

        assertThat(savedImages).allSatisfy(image -> {
            assertThat(image.getPostId()).isEqualTo(response.id());
            assertThat(image.getImageUrl()).isNotBlank();
        });
    }

    @Test
    @DisplayName("게시글 등록 테스트 | 2. 크리에이터지만 공개 게시판에 파일을 등록하는 경우")
    void createPost2() throws IOException {
        // given
        Long memberId = 5L;
        PostCreateRequest request = new PostCreateRequest("테스트 게시글", false);

        List<MultipartFile> images = List.of(
                createImage("image1.png"),
                createImage("image2.png")
        );
        MockMultipartFile file = createFile("test.pdf");

        // when & then
        assertThatThrownBy(() ->
                postService.createPost(memberId, request, images, file)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구독자 전용 게시글에만 첨부 파일을 업로드할 수 있습니다.");
    }

    @Test
    @DisplayName("게시글 등록 테스트 | 3. 크리에이터가 아닌 사용자가 파일을 등록하는 경우")
    void createPost3() throws IOException {
        // given
        Long memberId = 2L;
        PostCreateRequest request = new PostCreateRequest("테스트 게시글", false);

        List<MultipartFile> images = List.of(
                createImage("image1.png"),
                createImage("image2.png")
        );
        MockMultipartFile file = createFile("test.pdf");

        // when & then
        assertThatThrownBy(() ->
                postService.createPost(memberId, request, images, file)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("크리에이터만 첨부 파일을 업로드할 수 있습니다.");
    }

    @Test
    @DisplayName("게시글 등록 테스트 | 4. 크리에이터가 구독자 전용 게시판에 파일을 등록하는 경우")
    void createPost4() throws IOException {
        // given
        Long memberId = 5L;
        PostCreateRequest request = new PostCreateRequest("테스트 게시글", true);

        List<MultipartFile> images = List.of(
                createImage("image1.png"),
                createImage("image2.png")
        );
        MockMultipartFile file = createFile("test.pdf");
        // when
        PostCreateResponse response = postService.createPost(memberId, request, images, file);


        // then: 게시글 저장 확인
        Post savedPost = postMapper.findById(response.id());

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getMemberId()).isEqualTo(memberId);
        assertThat(savedPost.getContent()).isEqualTo("테스트 게시글");
        assertThat(savedPost.isSubscriberOnly()).isTrue();

        // then: 이미지 정보 저장 확인
        List<PostImage> savedImages =
                postImageMapper.findAllByPostId(response.id());

        assertThat(savedImages).hasSize(2);
        assertThat(savedImages)
                .extracting(PostImage::getImageOrder)
                .containsExactly(1, 2);

        assertThat(savedImages).allSatisfy(image -> {
            assertThat(image.getPostId()).isEqualTo(response.id());
            assertThat(image.getImageUrl()).isNotBlank();
        });

        // then: 파일 정보 저장 확인
        assertThat(savedPost.getFileUrl()).isNotBlank();
        assertThat(savedPost.getFileUrl()).endsWith(".pdf");
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

//    @Test
//    @DisplayName("게시글 파일도 수정 테스트")
//    void updateFileTest() throws IOException {
//        // given
//        PostCreateRequest createRequest = new PostCreateRequest("수정 테스트", true);
//        PostCreateResponse createResponse = postService.createPost(1L, createRequest, List.of(createImage("image1.png")), createFile("test.pdf"));
//        Long postId = createResponse.id();
//        String oldFileUrl = postMapper.findById(postId).getFileUrl();
//
//        // when
//        PostUpdateRequest updateRequest = new PostUpdateRequest("수정된 내용", true, false);
//        postService.updatePost(postId, 1L, updateRequest, createFile("updated.pdf"));
//
//        // then
//        Post updatedPost = postMapper.findById(postId);
//        assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");
//        assertThat(updatedPost.getFileUrl())
//                .isNotEqualTo(oldFileUrl)
//                        .startsWith("/uploads/posts/files/")
//                                .endsWith(".pdf");
//        assertThat(updatedPost.isSubscriberOnly()).isTrue();
//
//        fileStorageService.delete(updatedPost.getFileUrl());
//        fileStorageService.delete(oldFileUrl);
//    }

    // 게시글 생성
    private List<MultipartFile> createPost(Long memberId, String content, boolean subscriberOnly) throws IOException {
        PostCreateRequest postCreateRequest = new PostCreateRequest(content, subscriberOnly);

        List<MultipartFile> images = List.of(
                createImage("image1.png"),
                createImage("image2.png")
        );

        if (subscriberOnly) {
            MockMultipartFile file = createFile("test.pdf");
            postService.createPost(memberId, postCreateRequest, images, file);
        } else {
            postService.createPost(memberId, postCreateRequest, images, null);
        }

        return images;
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