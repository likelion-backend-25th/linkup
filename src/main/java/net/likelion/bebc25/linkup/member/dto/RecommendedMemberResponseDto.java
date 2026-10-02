package net.likelion.bebc25.linkup.member.dto;

public record RecommendedMemberResponseDto(
        Long id,
        String name,
        String uniqueId,
        String profileImage,
        String introduction,
        Integer followerCount,
        boolean following
) {
}
