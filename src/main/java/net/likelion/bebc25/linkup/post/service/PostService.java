package net.likelion.bebc25.linkup.post.service;

import net.likelion.bebc25.linkup.post.dto.PostCreateRequest;
import net.likelion.bebc25.linkup.post.dto.PostCreateResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostCreateResponse createPost(Long memberId, PostCreateRequest request, List<MultipartFile> images, MultipartFile file);
}
