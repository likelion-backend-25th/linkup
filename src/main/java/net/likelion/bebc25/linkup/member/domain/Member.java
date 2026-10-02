package net.likelion.bebc25.linkup.member.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    private Long id;

    private String email;
    private String password;
    private String name;
    private String uniqueId;

    private String profileImage;
    private String introduction;

    private String role;

    private int warningCount;
    private LocalDateTime writingRestrictedTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int followingCount;
    private int followerCount;
}
