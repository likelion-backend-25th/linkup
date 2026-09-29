package net.likelion.bebc25.linkup.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostDetailRow {
    // 게시글 ID
    private Long id;
    // 회원 ID
    private Long memberId;
    // 회원 이름
    private String name;
    // 화면에 보이는 회원 ID
    private String uniqueId;
    // 회원 프로필 사진 URL
    private String profileImage;
    // 게시글 본문
    private String content;
    // 게시글 파일 URL
    private String fileUrl;
    // 게시글 좋아요 카운트
    private int likeCount;
    // 구독자 전용 게시글 여부
    private boolean subscriberOnly;
    // 게시글 생성 일시
    private LocalDateTime createdAt;
    // 게시글 수정 일시
    private LocalDateTime updatedAt;
}
