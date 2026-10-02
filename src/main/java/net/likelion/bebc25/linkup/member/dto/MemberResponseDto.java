package net.likelion.bebc25.linkup.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// MyBatis 가 기본 생성자로 만든 뒤 컬럼 별칭 = 필드명으로 매핑하도록 한다.
// (생성자가 하나뿐이면 컬럼 "순서"로 생성자 인자에 넣어서, 필드 추가/순서 변경 시 깨진다.)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String uniqueId;
    private String name;
    private String profileImage;
    private String introduction;
    private String role;
    private int postCount;
    private int followerCount;
    private int followingCount;
    private String subscribedStatus;

    public void setSubscribedStatus(String subscribedStatus) {
        this.subscribedStatus = subscribedStatus;
    }
}