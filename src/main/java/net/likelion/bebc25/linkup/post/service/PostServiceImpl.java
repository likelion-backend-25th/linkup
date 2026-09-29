package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.mapper.MemberMapper;
import net.likelion.bebc25.linkup.post.domain.Post;
import net.likelion.bebc25.linkup.post.domain.PostImage;
import net.likelion.bebc25.linkup.post.dto.*;
import net.likelion.bebc25.linkup.post.mapper.PostImageMapper;
import net.likelion.bebc25.linkup.post.mapper.PostLikeMapper;
import net.likelion.bebc25.linkup.post.mapper.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostServiceImpl implements PostService {

    private static final int MAX_IMAGE_COUNT = 5;
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_FILE_SIZE = 100L *  1024 * 1024;
    private static final String POST_IMAGE_DIRECTORY = "assets/postImages/";
    private static final String POST_FILE_DIRECTORY = "assets/files/";

    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final FileStorageService fileStorageService;
    private final PostReadAccessService postReadAccessService;
    private final PostLikeMapper postLikeMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public PostCreateResponse createPost(Long memberId, PostCreateRequest request, List<MultipartFile> images, MultipartFile file) {
        Member member = memberMapper.findById(memberId);
        // 회원 존재 검사
        if (member == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."
            );
        }

        List<String> uploadedKeys = new ArrayList<>();
        registerUploadRollbackCleanUp(uploadedKeys); // 롤백 시 파일, 이미지 삭제
        validateImages(images); // 이미지 검증
        validateFile(file); // 파일 검증

        // 구독자 전용 게시글 게시 권한 검사
        if (request.subscriberOnly() && !isCreator(member)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "크리에이터만 구독자 전용 게시글을 작성할 수 있습니다.");
        }

        boolean hasFile  = file != null && !file.isEmpty();
        String fileKey = null;

        // 파일이 존재하는 경우
        if (hasFile) {
            validateFileAttachmentPermission(member, request.subscriberOnly());
            fileKey = fileStorageService.upload(file, POST_FILE_DIRECTORY);
            uploadedKeys.add(fileKey);
        }

        Post post = Post.builder()
                .memberId(memberId)
                .content(request.content())
                .fileUrl(fileKey)
                .subscriberOnly(request.subscriberOnly())
                .build();

        postMapper.insert(post);
        // 이미지 저장
        uploadAndSavePostImages(post.getId(), images, uploadedKeys);

        return PostCreateResponse.from(post);
    }

    @Override
    @Transactional
    public PostDetailResponse getPostDetailById(Long postId, Long memberId) {
        PostDetailRow postDetailRow = postMapper.findDetailById(postId);

        if (postDetailRow == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."
            );
        }

        // 구독자 전용 게시글일 경우
        if (postDetailRow.isSubscriberOnly()) {
            postReadAccessService.isSubscriber(memberId, postDetailRow.getMemberId());
        }

        List<PostImage> images = postImageMapper.findAllByPostId(postId);
        boolean likedByMe = postLikeMapper.existsByPostIdAndMemberId(postId, memberId);
        return PostDetailResponse.from(postDetailRow, images, likedByMe);
    }

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post post = postMapper.findById(postId);

        if (post == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."
            );
        }

        if (!post.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "게시글을 삭제할 권한이 없습니다."
            );
        }

        List<PostImage> postImages = postImageMapper.findAllByPostId(postId);
        List<String> fileReferences = new ArrayList<>();

        for (PostImage image : postImages) {
            fileReferences.add(image.getImageUrl());
        }

        if (post.getFileUrl() != null) {
            fileReferences.add(post.getFileUrl());
        }

        postMapper.deleteById(postId);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for (String reference : fileReferences) {
                            try {
                                fileStorageService.delete(reference);
                            } catch (RuntimeException e) {
                                log.error("게시글 파일 삭제 실패: {}", reference, e);
                            }
                        }
                    }
                }
        );
    }

    @Override
    @Transactional
    public void updatePost(Long postId, Long memberId, PostUpdateRequest request, List<MultipartFile> images, MultipartFile file) {
        // 게시글 조회 및 작성자 권한 확인
        Post existingPost = getOwnedPostOrThrow(postId, memberId);

        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."
            );
        }

        if (request.subscriberOnly() && !isCreator(member)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "크리에이터만 구독자 전용 게시글을 작성할 수 있습니다."
            );
        }

        // 새 이미지가 전달되지 않은 경우 빈 리스트로 처리
        List<MultipartFile> newImages = images == null ? List.of() : images;

        // 수정 전 기존 이미지 조회
        List<PostImage> existingImages = postImageMapper.findAllByPostId(postId);

        // 이미지 수정 요청 검증 ( imageRequests 요청 값 검증 )
        validateImageUpdateRequest(request.imageRequest(), newImages);

        // 새로 추가되는 이미지 파일 검증
        if (!newImages.isEmpty()) {
            validateImages(newImages);
        }

        // 트랜잭션 실패 시 새로 업로드한 파일을 삭제하기 위한 목록
        List<String> uploadedKeys = new ArrayList<>();
        registerUploadRollbackCleanUp(uploadedKeys);

        // 첨부파일 삭제, 유지, 교체 처리
        String newFileKey = resolveFileKey(existingPost, member, request, file, uploadedKeys);

        // 변경된 게시글 정보 저장
        Post post = Post.builder()
                .id(postId)
                .memberId(memberId)
                .content(request.content())
                .fileUrl(newFileKey)
                .subscriberOnly(request.subscriberOnly())
                .build();
        postMapper.updateById(post);

        // 이미지 유지 / 추가 / 삭제 / 순서 변경
        updatePostImages(
                postId,
                request.imageRequest(),
                existingImages,
                newImages,
                uploadedKeys
        );

        // DB 커밋 후 기존 첨부파일 삭제
        scheduleOldFileDeletion(existingPost.getFileUrl(), newFileKey);
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

    // 롤백 시 파일 정리
    private void registerUploadRollbackCleanUp(List<String> uploadedKeys) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status != STATUS_ROLLED_BACK) {
                            return;
                        }
                        for (String key : uploadedKeys) {
                            try {
                                fileStorageService.delete(key);
                            } catch (RuntimeException e) {
                                log.error("롤백 후 업로드 파일 정리 실패: {}", key, e);
                            }
                        }
                    }
                }
        );
    }

    // 파일 첨부 규칙 검사
    private void validateFileAttachmentPermission(Member member, boolean subscriberOnly) {
        if (!member.getRole().equals("ROLE_CREATOR")) {
            throw new IllegalArgumentException("크리에이터만 첨부 파일을 업로드할 수 있습니다.");
        }

        if (!subscriberOnly) {
            throw new IllegalArgumentException("구독자 전용 게시글에만 첨부 파일을 업로드할 수 있습니다.");
        }
    }

    // 이미지 저장
    private void uploadAndSavePostImages(Long postId, List<MultipartFile> images, List<String> uploadedKeys) {
        for (int i = 0; i < images.size(); i++) {
            String imageKey = fileStorageService.upload(images.get(i), POST_IMAGE_DIRECTORY);
            uploadedKeys.add(imageKey);

            PostImage postImage = PostImage.builder()
                    .postId(postId)
                    .imageUrl(imageKey)
                    .imageOrder(i+1)
                    .build();

            postImageMapper.insert(postImage);
        }
    }

    // 크리에이터인지 확인 (임시)
    private boolean isCreator(Member member) {
        return member.getRole().equals("ROLE_CREATOR");
    }

    // 게시글 조회, 작성자 본인 확인
    private Post getOwnedPostOrThrow(Long postId, Long memberId) {
        Post existingPost = postMapper.findById(postId);
        if (existingPost == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."
            );
        }

        if (!existingPost.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "게시글을 수정할 권한이 없습니다."
            );
        }

        return existingPost;
    }

    // 첨부파일 삭제,유지,교체 요청을 처리하고, DB에 저장할 파일 키 반환
    // 새로 업로드한 파일 키는 롤백 시 정리할 목록에 추가
    private String resolveFileKey(Post existingPost,
                                  Member member,
                                  PostUpdateRequest request,
                                  MultipartFile file,
                                  List<String> uploadedKeys) {
        boolean hasFile = file != null && !file.isEmpty();

        if (request.removeFile() && hasFile) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "파일 삭제와 새 파일 첨부를 동시에 요청할 수 없습니다."
            );
        }

        // 삭제: DB에 저장할 파일 키를 null로 반환
        if (request.removeFile()) {
            return null;
        }

        // 유지
        if (!hasFile) {
            String oldKey = existingPost.getFileUrl();
            if (oldKey != null && !request.subscriberOnly()) {
                throw new IllegalArgumentException("첨부 파일이 있는 게시글은 구독자 전용이어야 합니다.");
            }
            return oldKey;
        }

        // 교체
        validateFile(file);
        validateFileAttachmentPermission(member, request.subscriberOnly());
        String newKey = fileStorageService.upload(file, POST_FILE_DIRECTORY);
        uploadedKeys.add(newKey);
        return newKey;
    }

    // 첨부파일이 교체되거나 제거되면, DB 커밋 후 기존 파일 삭제 예약
    private void scheduleOldFileDeletion(String oldKey, String fileKey) {
        // 기존 파일이 없거나 유지되는 경우에는 삭제하지 않음
        if (oldKey == null || !oldKey.equals(fileKey)) {
            return;
        }

        // DB 롤백 시 기존 파일이 필요하므로, 커밋이 확정된 후에만 삭제
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            fileStorageService.delete(oldKey);
                        } catch (RuntimeException e) {
                            // DB는 이미 커밋됐으므로 정리 실패 로그 남김
                            log.error("기존 첨부파일 삭제 실패: {}", oldKey, e);
                        }
                    }
                }
        );
    }

    // 이미지 제거되면, DB 커밋 후 기존 파일 삭제 예약
    private void scheduleImageDeletionAfterCommit(String key) {

        // DB 롤백 시 기존 파일이 필요하므로, 커밋이 확정된 후에만 삭제
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            fileStorageService.delete(key);
                        } catch (RuntimeException e) {
                            // DB는 이미 커밋됐으므로 정리 실패 로그 남김
                            log.error("기존 첨부파일 삭제 실패: {}", key, e);
                        }
                    }
                }
        );
    }

    // imageRequests 요청 값 검증
    private void validateImageUpdateRequest(
            List<PostImageUpdateRequest> imageRequests,
            List<MultipartFile> newImages
    ) {
        Set<Integer> usedIndexes = new HashSet<>();

        for (PostImageUpdateRequest imageRequest : imageRequests) {
            Long imageId = imageRequest.imageId();
            Integer newImageIndex = imageRequest.newImageIndex();

            if ((imageId != null) == (newImageIndex != null)) {
                throw new IllegalArgumentException(
                        "기존 이미지 ID와 새 이미지 인덱스 중 하나만 지정해야 합니다."
                );
            }

            // 기존 이미지인 경우 새 이미지 검증 불필요
            if (newImageIndex == null) {
                continue;
            }

            // 실제 업로드된 새 이미지의 범위를 벗어나는 지 확인
            if (newImageIndex < 0 || newImageIndex >= newImages.size()) {
                throw new IllegalArgumentException(
                        "새 이미지 인덱스가 업로드된 이미지 범위를 벗어났습니다."
                );
            }

            // 동일한 새 이미지를 여러 번 사용하는 지 확인
            if (!usedIndexes.add(newImageIndex)) {
                throw new IllegalArgumentException(
                        "동일한 새 이미지를 중복해서 사용할 수 없습니다."
                );
            }
        }
    }

    // 게시글 이미지 수정
    private void updatePostImages(
            Long postId,
            List<PostImageUpdateRequest> imageRequests,
            List<PostImage> existingImages,
            List<MultipartFile> newImages,
            List<String> uploadedKeys
    ) {
        // 수정 후에도 유지할 기존 이미지 ID 저장
        Set<Long> keptImageIds = new HashSet<>();

        // 최종 이미지 목록 순서대로 기존 이미지 유지 또는 새 이미지 추가
        for (int i = 0; i < imageRequests.size(); i++) {
            PostImageUpdateRequest imageRequest = imageRequests.get(i);

            Long imageId = imageRequest.imageId();
            Integer newImageIndex = imageRequest.newImageIndex();

            // 기존 이미지인 경우 유지 대상이며 이미지 순서 변경
            if (imageId != null) {
                keptImageIds.add(imageId);
                postImageMapper.updateImageOrder(imageId, i + 1);
            }

            // 새 이미지인 경우 업로드 후 게시글 이미지로 저장
            if (newImageIndex != null) {
                MultipartFile newImage = newImages.get(newImageIndex);
                String imageUrl = fileStorageService.upload(newImage, POST_IMAGE_DIRECTORY);
                // DB 작업 실패 시 업로드한 파일 삭제할 수 있도록 추가
                uploadedKeys.add(imageUrl);

                PostImage postImage = PostImage.builder()
                        .postId(postId)
                        .imageUrl(imageUrl)
                        .imageOrder(i+1)
                        .build();
                postImageMapper.insert(postImage);

            }
        }

        // 최종 이미지 목록에 포함되지 않은 기존 이미지 삭제
        for (PostImage existingImage : existingImages) {
            if (!keptImageIds.contains(existingImage.getId())) {
                postImageMapper.deleteById(existingImage.getId());
                // DB 커밋 성공 후 실제 저장소의 이미지 삭제
                scheduleImageDeletionAfterCommit(existingImage.getImageUrl());
            }
        }
    }
}
