package net.likelion.bebc25.linkup.member.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private Long id;

    private String email;
    private String password;
    private String name;
    private String uniqueId;

    private String profileImage;
    private String introduction;

    private String role;

    private int warning_count;
    private LocalDateTime writing_restricted_time;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
