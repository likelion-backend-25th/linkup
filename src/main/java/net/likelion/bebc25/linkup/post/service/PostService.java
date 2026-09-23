package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.FollowingFeedResponse;
import net.likelion.bebc25.linkup.post.dto.PostCreateRequest;
import net.likelion.bebc25.linkup.post.dto.PostCreateResponse;
import net.likelion.bebc25.linkup.post.dto.PostDetailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    // 게시글 등록
    PostCreateResponse createPost(Long memberId, PostCreateRequest request, List<MultipartFile> images, MultipartFile file);

    // 게시글 단 건 조회
    PostDetailResponse getPostDetailById(Long id);
}
