package net.likelion.bebc25.linkup.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import net.likelion.bebc25.linkup.follow.mapper.FollowMapper;
import net.likelion.bebc25.linkup.member.block.mapper.BlockMapper;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostServiceImpl implements PostService {

    private static final int MAX_IMAGE_COUNT = 5;
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_FILE_SIZE_MB = 100;
    private static final long MAX_FILE_SIZE = MAX_FILE_SIZE_MB *  1024 * 1024;
    private static final String POST_IMAGE_DIRECTORY = "uploads/posts/";
    private static final String POST_FILE_DIRECTORY = "uploads/files/";
    private static final String ROLE_CREATOR = "ROLE_CREATOR";

    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final FileStorageService fileStorageService;
    private final FollowMapper followMapper;
    private final PostLikeMapper postLikeMapper;
    private final MemberMapper memberMapper;
    private final BlockMapper blockMapper;

    @Override
    @Transactional
    public PostCreateResponse createPost(Long memberId,
                                         PostCreateRequest request,
                                         List<MultipartFile> images,
                                         MultipartFile file) {
        // 작성자와 게시글 생성 요청의 유효성을 확인한다.
        Member member = getMemberOrThrow(memberId);
        validateCreatePostRequest(member, request, images, file);

        // DB 작업 실패 시 이미 업로드된 파일을 정리하기 위해 추적한다.
        List<String> uploadedKeys = new ArrayList<>();
        registerUploadedFileCleanupOnRollback(uploadedKeys);

        // 선택 첨부파일을 먼저 업로드하고, 게시글에는 저장소 key만 기록한다.
        String fileKey = uploadPostFileIfPresent(member, request, file, uploadedKeys);

        // 게시글을 저장한 뒤, 생성된 게시글 ID로 이미지를 업로드 및 저장한다.
        Post post = createPostEntity(memberId, request, fileKey);
        postMapper.insert(post);

        uploadAndSavePostImages(post.getId(), images, uploadedKeys);

        return PostCreateResponse.from(post);
    }

    @Override
    @Transactional
    public PostDetailResponse getPostDetailById(Long postId, Long memberId) {
        PostDetailRow postDetailRow = getPostDetailRowOrThrow(postId);
        validatePostReadAccess(postDetailRow, memberId);

        // 상세 화면에 필요한 부가 정보를 함께 조회한다.
        List<PostImage> images = postImageMapper.findAllByPostId(postId);
        boolean likedByMe = postLikeMapper.existsByPostIdAndMemberId(postId, memberId);
        boolean followingAuthor = isFollowingAuthor(memberId, postDetailRow.getMemberId());

        return PostDetailResponse.from(postDetailRow, images, likedByMe, followingAuthor);
    }

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post post = getOwnedPostOrThrow(postId, memberId);
        List<String> fileReferences = collectPostFileReferences(post);

        postMapper.deleteById(postId);

        // DB 삭제가 커밋된 뒤 실제 저장소 파일을 삭제한다.
        scheduleFileDeletionAfterCommit(fileReferences);
    }

    @Override
    @Transactional
    public void updatePost(Long postId,
                           Long memberId,
                           PostUpdateRequest request,
                           List<MultipartFile> images,
                           MultipartFile file) {
        Post existingPost = getOwnedPostOrThrow(postId, memberId);
        Member member = getMemberOrThrow(memberId);

        List<MultipartFile> newImages = emptyIfNull(images);
        List<PostImage> existingImages = postImageMapper.findAllByPostId(postId);

        validateUpdatePostRequest(member, request, newImages);

        // 수정 중 새로 업로드된 파일은 DB 작업이 실패하면 롤백 정리한다.
        List<String> uploadedKeys = new ArrayList<>();
        registerUploadedFileCleanupOnRollback(uploadedKeys);

        String newFileKey = resolveFileKey(existingPost, member, request, file, uploadedKeys);

        Post updatePost = createUpdatedPostEntity(postId, memberId, request, newFileKey);
        postMapper.updateById(updatePost);

        updatePostImages(
                postId,
                request.imageRequest(),
                existingImages,
                newImages,
                uploadedKeys
        );

        // DB 수정이 확정된 뒤 더 이상 참조하지 않는 기존 첨부파일을 삭제한다.
        scheduleOldFileDeletion(existingPost.getFileUrl(), newFileKey);
    }

    // 업로드 가능한 이미지 개수, 용량, 실제 이미지 여부를 검증한다.
    private void validateImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("이미지를 1개 이상 업로드해야 합니다.");
        }

        if (images.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(
                    "이미지는 최대 " + MAX_IMAGE_COUNT + "개까지 업로드할 수 있습니다."
            );
        }

        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("빈 이미지 파일은 업로드할 수 없습니다.");
            }

            if (image.getSize() > MAX_IMAGE_SIZE) {
                throw new IllegalArgumentException(
                        "이미지는 최대 " + MAX_IMAGE_SIZE + "바이트까지 업로드할 수 있습니다."
                );
            }

            try (InputStream inputStream = image.getInputStream()) {
                if (ImageIO.read(inputStream) == null) {
                    throw new IllegalArgumentException("지원하지 않는 형식이거나 올바른 이미지 파일이 아닙니다.");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("이미지 파일을 읽을 수 없습니다.", e);
            }
        }
    }

    // 선택 첨부파일의 최대 용량을 검증한다.
    private void validateAttachmentFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("첨부 파일은 최대 " + MAX_FILE_SIZE_MB + "MB까지 업로드할 수 있습니다.");
        }
    }

    // DB 트랜잭션이 롤백되면 이번 요청에서 새로 업로드한 파일을 정리한다.
    private void registerUploadedFileCleanupOnRollback(List<String> uploadedKeys) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status != STATUS_ROLLED_BACK) {
                            return;
                        }
                        deleteFilesSilently(uploadedKeys);
                    }
                }
        );
    }

    // 첨부파일은 크리에이터의 구독자 전용 게시글에만 허용한다.
    private void validateFileAttachmentPermission(Member member, boolean subscriberOnly) {
        if (!isCreator(member)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "크리에이터만 첨부파일을 업로드할 수 있습니다."
            );
        }

        if (!subscriberOnly) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "첨부파일은 구독자 전용 게시글에만 업로드할 수 있습니다."
            );
        }
    }

    // 게시글 이미지들을 업로드하고, 생성된 저장소 key를 DB에 저장한다.
    private void uploadAndSavePostImages(Long postId,
                                         List<MultipartFile> images,
                                         List<String> uploadedKeys) {
        for (int index = 0; index < images.size(); index++) {
            MultipartFile image = images.get(index);
            String imageKey = fileStorageService.upload(image, POST_IMAGE_DIRECTORY);

            uploadedKeys.add(imageKey);
            postImageMapper.insert(createPostImage(postId, imageKey, index + 1));
        }
    }

    // 크리에이터인지 확인 (임시)
    private boolean isCreator(Member member) {
        return ROLE_CREATOR.equals(member.getRole());
    }

    // 게시글 조회, 작성자 본인 확인
    private Post getOwnedPostOrThrow(Long postId, Long memberId) {
        Post post = postMapper.findById(postId);

        if (post == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게시글이 존재하지 않습니다."
            );
        }

        if (!post.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "게시글에 대한 권한이 없습니다."
            );
        }

        return post;
    }

    // 게시글 수정 요청에 따라 DB에 저장할 첨부파일 key를 결정한다.
    private String resolveFileKey(Post existingPost,
                                  Member member,
                                  PostUpdateRequest request,
                                  MultipartFile file,
                                  List<String> uploadedKeys) {
        boolean hasFile = file != null && !file.isEmpty();

        if (request.removeFile() && hasFile) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "파일 삭제와 새 파일 첨부를 동시에 요청할 수 없습니다."
            );
        }

        // 삭제: DB에 저장할 파일 키를 null로 반환
        if (request.removeFile()) {
            return null;
        }

        // 유지
        if (!hasFile) {
            String oldFileKey = existingPost.getFileUrl();

            if (oldFileKey != null && !request.subscriberOnly()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "첨부파일이 있는 게시글은 구독자 전용이어야 합니다."
                );
            }

            return oldFileKey;
        }

        // 교체
        validateAttachmentFile(file);
        validateFileAttachmentPermission(member, request.subscriberOnly());

        String newFileKey = fileStorageService.upload(file, POST_FILE_DIRECTORY);
        uploadedKeys.add(newFileKey);

        return newFileKey;
    }


    // DB 커밋 후 더 이상 참조하지 않는 기존 첨부파일을 삭제한다.
    private void scheduleOldFileDeletion(String oldFileKey, String newFileKey) {
        // 기존 파일이 없거나 유지되는 경우에는 삭제하지 않음
        if (oldFileKey == null || oldFileKey.equals(newFileKey)) {
            return;
        }

        scheduleFileDeletionAfterCommit(oldFileKey);
    }

    // 이미지 수정 요청이 기존 이미지 또는 새 이미지 중 하나만 참조하는지 검증한다.
    private void validateImageUpdateRequests(
            List<PostImageUpdateRequest> imageRequests,
            List<MultipartFile> newImages
    ) {
        Set<Integer> usedNewImageIndexes = new HashSet<>();

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
            if (!usedNewImageIndexes.add(newImageIndex)) {
                throw new IllegalArgumentException(
                        "동일한 새 이미지를 중복해서 사용할 수 없습니다."
                );
            }
        }
    }

    // 이미지 수정 요청의 최종 순서에 맞춰 기존 이미지를 유지하거나 새 이미지를 추가한다.
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
                scheduleFileDeletionAfterCommit(existingImage.getImageUrl());
            }
        }
    }

    // 회원이 존재하는지 확인하고 조회한다.
    private Member getMemberOrThrow(Long memberId) {
        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."
            );
        }
        return member;
    }

    // 게시글 생성 요청의 이미지, 첨부파일, 작성 권한을 검증한다.
    private void validateCreatePostRequest(
            Member member,
            PostCreateRequest request,
            List<MultipartFile> images,
            MultipartFile file
    ) {
        validateImages(images);
        validateAttachmentFile(file);
        validateSubscriberOnlyPostPermission(member, request.subscriberOnly());
    }

    // 구독자 전용 게시글은 크리에이터만 작성할 수 있다.
    private void validateSubscriberOnlyPostPermission(Member member, boolean subscriberOnly) {
        if (subscriberOnly && !isCreator(member)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "크리에이터만 구독자 전용 게시글을 작성할 수 있습니다."
            );
        }
    }

    // 파일이 첨부된 경우 권한을 확인한 뒤 저장소에 업로드한다.
    private String uploadPostFileIfPresent(
            Member member,
            PostCreateRequest request,
            MultipartFile file,
            List<String> uploadedKeys
    ) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        validateFileAttachmentPermission(member, request.subscriberOnly());

        String fileKey = fileStorageService.upload(file, POST_FILE_DIRECTORY);
        uploadedKeys.add(fileKey);

        return fileKey;
    }

    // 게시글 생성 요청으로 저장할 Post 엔티티를 만든다.
    private Post createPostEntity(
            Long memberId,
            PostCreateRequest request,
            String fileKey
    ) {
        return Post.builder()
                .memberId(memberId)
                .content(request.content())
                .fileUrl(fileKey)
                .subscriberOnly(request.subscriberOnly())
                .build();
    }

    // 게시글 상세 정보를 조회하고 없으면 예외를 던진다.
    private PostDetailRow getPostDetailRowOrThrow(Long postId) {
        PostDetailRow postDetailRow = postMapper.findDetailById(postId);

        if (postDetailRow == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게시글이 존재하지 않습니다."
            );
        }

        return postDetailRow;
    }

    // 구독자 전용 게시글은 작성자 또는 유효한 구독자만 조회할 수 있다.
    private void validatePostReadAccess(PostDetailRow postDetailRow, Long memberId) {
        Long authorId = postDetailRow.getMemberId();

        if (!memberId.equals(authorId) && blockMapper.existsBlock(memberId, authorId) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게시글이 존재하지 않습니다."
            );
        }

        if (!postDetailRow.isSubscriberOnly()) {
            return;
        }

        if (memberId.equals(authorId)) {
            return;
        }

        if (!postMapper.existsValidSubscription(memberId, authorId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "구독하지 않은 게시글입니다."
            );
        }
    }

    // 게시글 삭제 후 저장소에서 제거할 이미지와 첨부파일 key를 수집한다.
    private List<String> collectPostFileReferences(Post post) {
        List<String> fileReferences = new ArrayList<>();

        List<PostImage> postImages = postImageMapper.findAllByPostId(post.getId());
        for (PostImage image : postImages) {
            fileReferences.add(image.getImageUrl());
        }

        if (post.getFileUrl() != null) {
            fileReferences.add(post.getFileUrl());
        }

        return fileReferences;
    }

    // DB 변경이 커밋된 뒤 더 이상 참조하지 않는 저장소 파일을 삭제한다.
    private void scheduleFileDeletionAfterCommit(String fileReference) {
        if (fileReference == null) {
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        deleteFileSilently(fileReference);
                    }
                }
        );
    }

    // DB 변경이 커밋된 뒤 더 이상 참조하지 않는 저장소 파일들을 삭제한다.
    private void scheduleFileDeletionAfterCommit(List<String> fileReferences) {
        if (fileReferences.isEmpty()) {
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for (String reference : fileReferences) {
                            deleteFileSilently(reference);
                        }
                    }
                }
        );
    }

    // 파일 삭제 실패가 DB 작업 결과에 영향을 주지 않도록 로그만 남긴다.
    private void deleteFilesSilently(List<String> fileReferences) {
        for (String reference : fileReferences) {
            deleteFileSilently(reference);
        }
    }

    private void deleteFileSilently(String reference) {
        try {
            fileStorageService.delete(reference);
        } catch (RuntimeException e) {
            log.error("게시글 파일 삭제 실패: {}", reference, e);
        }
    }

    // null 이미지 목록을 빈 목록으로 바꿔 이후 로직에서 null 체크를 없앤다.
    private List<MultipartFile> emptyIfNull(List<MultipartFile> images) {
        return images == null ? List.of() : images;
    }

    // 게시글 수정 요청의 권한, 이미지 수정 요청, 새 이미지 파일을 검증한다.
    private void validateUpdatePostRequest(
            Member member,
            PostUpdateRequest request,
            List<MultipartFile> newImages
    ) {
        List<PostImageUpdateRequest> imageRequests =
                request.imageRequest() == null ? List.of() : request.imageRequest();

        validateSubscriberOnlyPostPermission(member, request.subscriberOnly());
        validateImageUpdateRequests(imageRequests, newImages);

        if (!newImages.isEmpty()) {
            validateImages(newImages);
        }
    }

    private Post createUpdatedPostEntity(
            Long postId,
            Long memberId,
            PostUpdateRequest request,
            String fileKey
    ) {
        return Post.builder()
                .id(postId)
                .memberId(memberId)
                .content(request.content())
                .fileUrl(fileKey)
                .subscriberOnly(request.subscriberOnly())
                .build();
    }

    private PostImage createPostImage(Long postId, String imageKey, int imageOrder) {
        return PostImage.builder()
                .postId(postId)
                .imageUrl(imageKey)
                .imageOrder(imageOrder)
                .build();
    }

    // 작성자 본인 게시글에는 팔로우 버튼 상태가 필요 없으므로 false를 반환한다
    private boolean isFollowingAuthor(Long memberId, Long authorId) {
        if (memberId.equals(authorId)) {
            return false;
        }

        return followMapper.isFollowing(memberId, authorId);
    }
}
