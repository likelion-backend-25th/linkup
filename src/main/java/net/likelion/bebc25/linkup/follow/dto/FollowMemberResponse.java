package net.likelion.bebc25.linkup.follow.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowMemberResponse {
    private Long followId;
    private Long memberId;
    private String name;
    private String uniqueId;
    private String profileImage;
}
