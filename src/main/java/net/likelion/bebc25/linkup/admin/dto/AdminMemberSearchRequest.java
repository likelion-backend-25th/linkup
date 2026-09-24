package net.likelion.bebc25.linkup.admin.dto;

public record AdminMemberSearchRequest(

        // 검색어
        String keyword,

        // 검색 타입(닉네임 / 아이디)
        String searchType,

        // 일반 회원 상태
        String memberStatus,

        // 크리에이터 상태
        String creatorStatus,

        int page,

        int size
) {
    public int offset(){
        return (page -1) * size;
    }
}
