package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("local")
public class S3FileStorageServiceTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    @DisplayName("파일을 S3에 업로드한다")
    void upload() {
        // given
        String directory = "posts/files";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "S3 업로드 테스트".getBytes(StandardCharsets.UTF_8)
        );

        // when
        String key = fileStorageService.upload(file, directory);

        // then
        assertThat(key).startsWith("/uploads/");
    }
}
