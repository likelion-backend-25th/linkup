package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.domain.PostImage;
import net.likelion.bebc25.linkup.post.dto.PostCreateRequest;
import net.likelion.bebc25.linkup.post.dto.PostCreateResponse;
import net.likelion.bebc25.linkup.post.mapper.PostImageMapper;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private static final int MAX_IMAGE_COUNT = 5;
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_FILE_SIZE = 100L *  1024 * 1024;
    private static final String POST_IMAGE_DIRECTORY = "posts/images";
    private static final String POST_FILE_DIRECTORY = "posts/files";

    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public PostCreateResponse createPost(Long memberId, PostCreateRequest request, List<MultipartFile> images, MultipartFile file) {

        validateImages(images);
        validateFile(file);

        boolean hasFile  = file != null && !file.isEmpty();
        String imageKey = null;
        String fileKey = null;

        if (hasFile) {
            if (!isCreator(memberId)) {
                throw new IllegalArgumentException("크리에이터만 첨부 파일을 업로드할 수 있습니다.");
            }
            if (!request.subscriberOnly()) {
                throw new IllegalArgumentException("첨부 파일은 구독자 전용 게시글에만 업로드할 수 있습니다.");
            }
            fileKey = fileStorageService.upload(file, POST_FILE_DIRECTORY);
        }

        Post post = Post.builder()
                .memberId(memberId)
                .content(request.content())
                .fileUrl(fileKey)
                .subscriberOnly(request.subscriberOnly())
                .build();

        postMapper.insert(post);

        for (int i = 0; i < images.size(); i++) {
            imageKey = fileStorageService.upload(images.get(i), POST_IMAGE_DIRECTORY);

            PostImage postImage = PostImage.builder()
                    .postId(post.getId())
                    .imageUrl(imageKey)
                    .imageOrder(i)
                    .build();

            postImageMapper.insert(postImage);
        }


        return PostCreateResponse.from(post);
    }

    // 이미지 파일 검증
    private void validateImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("이미지를 1개 이상 업로드 해야 합니다.");
        }

        if (images.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("이미지는 최대 " + MAX_IMAGE_COUNT + "개까지 업로드할 수 있습니다.");
        }

        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("빈 이미지 파일은 업로드할 수 없습니다.");
            }

            if (image.getSize() > MAX_IMAGE_SIZE) {
                throw new IllegalArgumentException("이미지의 최대 용량은" + MAX_IMAGE_SIZE + "입니다.");
            }

            try (InputStream inputStream = image.getInputStream()) {
                BufferedImage bufferedImage = ImageIO.read(inputStream);

                if (bufferedImage == null) {
                    throw new IllegalArgumentException("지원하지 않는 형식이거나 올바른 이미지 파일이 아닙니다.");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("이미지 파일을 읽을 수 없습니다.", e);
            }
        }
    }

    // 첨부 파일 검증
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("첨부 파일의 최대 용량은" + MAX_FILE_SIZE + "입니다.");
        }
    }

    // 크리에이터인지 확인 (임시)
    private boolean isCreator(Long memberId) {
        return memberId == 1L;
    }
}
