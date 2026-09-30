package net.likelion.bebc25.linkup.member.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.dto.MemberDto;
import net.likelion.bebc25.linkup.member.dto.MemberResponseDto;
import net.likelion.bebc25.linkup.member.dto.MemberUpdateRequest;
import net.likelion.bebc25.linkup.member.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public MemberDto getMyInfo(Long memberId) {

        Member member = memberMapper.findById(memberId);

        if (member == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        int postCount = memberMapper.countPosts(memberId);

        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getUniqueId(),
                member.getProfileImage(),
                member.getIntroduction(),
                postCount,
                member.getFollower_count(),
                member.getFollowing_count()
        );
    }

    @Transactional
    public void updateMyProfile(Long memberId, MemberUpdateRequest request, MultipartFile profileImage) {
        Member member = memberMapper.findById(memberId);

        if (member == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        member.setName(request.getName());
        member.setIntroduction(request.getIntroduction());

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileStorageService.upload(profileImage, "uploads/profiles/");
            member.setProfileImage(imageUrl);
        }

        memberMapper.updateProfile(member);
    }

    public MemberResponseDto getMemberProfile(Long memberId) {

        MemberResponseDto profile =
                memberMapper.findProfileById(memberId);

        if (profile == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        return profile;
    }
}