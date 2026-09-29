package net.likelion.bebc25.linkup.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String name;
    private String uniqueId;
    private String profileImage;
    private String introduction;
    private int postCount;
    private int followerCount;
    private int followingCount;
}