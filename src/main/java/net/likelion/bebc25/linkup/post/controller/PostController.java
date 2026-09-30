package net.likelion.bebc25.linkup.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import net.likelion.bebc25.linkup.post.dto.PostCreateRequest;
import net.likelion.bebc25.linkup.post.dto.PostCreateResponse;
import net.likelion.bebc25.linkup.post.dto.PostDetailResponse;
import net.likelion.bebc25.linkup.post.dto.PostUpdateRequest;
import net.likelion.bebc25.linkup.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 등록", description = "게시글을 등록하고 게시글 ID를 응답합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 등록 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미지/첨부파일 검증 실패",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "403", description = "구독자 전용 게시글 작성 권한 없음", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PostCreateResponse> createPost(
            @Valid @RequestPart("request") PostCreateRequest request,
            @RequestPart(value = "images") List<MultipartFile> images,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();

        PostCreateResponse response = postService.createPost(memberId, request, images, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "게시글 단 건 상세 조회", description = "게시글 ID에 해당하는 게시글의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "403", description = "게시글에 접근할 수 없음", content = @Content)
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();
        PostDetailResponse response = postService.getPostDetailById(postId, memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 삭제", description = "게시글 ID에 해당하는 게시글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "게시글 삭제 권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음", content = @Content)
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();

        postService.deletePost(memberId, postId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 수정", description = "게시글 ID에 해당하는 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 수정 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미지/첨부파일 검증 실패",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "게시글 수정 권한 없음 또는 구독자 전용 게시글 작성 권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "게시글 또는 회원이 존재하지 않음", content = @Content)
    })
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @Valid @RequestPart("request")PostUpdateRequest request,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        Long memberId = userDetails.getId();
        postService.updatePost(postId, memberId, request, newImages, file);

        return ResponseEntity.noContent().build();
    }
}
