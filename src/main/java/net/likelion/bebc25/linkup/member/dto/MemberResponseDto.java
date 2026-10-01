package net.likelion.bebc25.linkup.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {

    private Long id;
    private String uniqueId;
    private String name;
    private String profileImage;
    private String introduction;

    private int followerCount;
    private int followingCount;

    private int postCount;

    private boolean creator;
    private Integer subscriptionPrice;
}