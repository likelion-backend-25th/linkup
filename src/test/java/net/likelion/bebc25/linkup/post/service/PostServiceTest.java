package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.PostCreateRequest;
import net.likelion.bebc25.linkup.post.dto.PostCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("일반 사용자는 여러 이미지를 등록할 수 있지만 파일 첨부는 할 수 없음")
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
        PostCreateResponse response = postService.createPost(memberId, request, images, null);

        // 2. 첨부 파일이 있으면 등록 거절
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                "file".getBytes()
        );

        // then
        // 1. 첨부 파일이 없으면 등록 성공
        assertThat(response).isNotNull();
        // 2. 첨부 파일이 있으면 등록 거절
        assertThatThrownBy(() -> postService.createPost(memberId, request, images, file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("크리에이터만 첨부 파일을 업로드할 수 있습니다.");
    }

    private MockMultipartFile createImage(String fileName) throws IOException {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);

        return new MockMultipartFile("images", fileName, "image/png", output.toByteArray());
    }
}