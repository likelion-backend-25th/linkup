package net.likelion.bebc25.linkup.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.post.dto.PostCreateRequest;
import net.likelion.bebc25.linkup.post.dto.PostCreateResponse;
import net.likelion.bebc25.linkup.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostCreateResponse> createPost(
            @Valid @RequestPart("request") PostCreateRequest request,
            @RequestPart(value = "images") List<MultipartFile> images,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        Long memberId = 1L;

        PostCreateResponse response = postService.createPost(memberId, request, images, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
