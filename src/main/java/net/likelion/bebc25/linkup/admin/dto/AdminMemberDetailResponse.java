package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDateTime;

public record AdminMemberDetailResponse(
        // 회원 ID
        Long id,

        // 프로필 이미지
        String profileImage,

        // 닉네임
        String nickname,

        // 회원 아이디
        String userId,

        // 이메일
        String email,

        // 일반 회원 상태
        String memberStatus,

        // 크리에이터 상태
        String creatorStatus,

        // 가입일
        LocalDateTime createdAt,

        // 구독 상품 가격 (테스트로 인해 int > Integer 변경)
        Integer subscriptionPrice,

        // 구독자 수
        Integer subscriberCount
) {
}
