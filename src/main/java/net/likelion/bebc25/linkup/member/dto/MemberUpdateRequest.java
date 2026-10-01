package net.likelion.bebc25.linkup.member.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {
    private String name;
    private String introduction;
    private String uniqueId;
}
