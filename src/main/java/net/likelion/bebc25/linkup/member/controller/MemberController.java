package net.likelion.bebc25.linkup.member.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.dto.BlockedMemberResponseDto;
import net.likelion.bebc25.linkup.member.dto.MemberDto;
import net.likelion.bebc25.linkup.member.dto.MemberResponseDto;
import net.likelion.bebc25.linkup.member.dto.MemberUpdateRequest;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import net.likelion.bebc25.linkup.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public MemberDto getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return memberService.getMyInfo(userDetails.getId());
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("request") MemberUpdateRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {

        memberService.updateMyProfile(userDetails.getId(), request, profileImage);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMemberProfile(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(
                memberService.getMemberProfile(memberId)
        );
    }

    @PostMapping("/{memberId}/block")
    public ResponseEntity<Void> blockMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memberId
    ) {
        memberService.blockMember(userDetails.getId(), memberId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}/block")
    public ResponseEntity<Void> unblockMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memberId
    ) {
        memberService.unblockMember(userDetails.getId(), memberId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<BlockedMemberResponseDto>> getBlockedMembers(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                memberService.getBlockedMembers(userDetails.getId())
        );
    }
}
