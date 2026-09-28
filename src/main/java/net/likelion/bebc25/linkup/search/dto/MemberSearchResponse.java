package net.likelion.bebc25.linkup.search.dto;

public record MemberSearchResponse(
        Long id,
        String name,
        String uniqueId,
        String profileImage
) {
}