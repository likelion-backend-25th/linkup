package net.likelion.bebc25.linkup.admin.dto;

import java.time.LocalDateTime;

public record AdminMemberResponse(

        // 아이디
        Long id,

        // 프로필 이미지
        String profileImage,

        // 닉네임
        String nickname,

        // 회원 아이디
        String userId,

        // 회원 상태
        String memberStatus,

        // 크리에이터 상태
        String creatorStatus,

        // 가입일
        LocalDateTime createdAt
){
}
