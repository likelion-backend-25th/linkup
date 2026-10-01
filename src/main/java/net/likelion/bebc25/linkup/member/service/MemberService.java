package net.likelion.bebc25.linkup.member.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.common.storage.FileStorageService;
import net.likelion.bebc25.linkup.follow.mapper.FollowMapper;
import net.likelion.bebc25.linkup.member.block.mapper.BlockMapper;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.dto.*;
import net.likelion.bebc25.linkup.member.mapper.MemberMapper;
import net.likelion.bebc25.linkup.subscription.mapper.SubscriptionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final FileStorageService fileStorageService;
    private final BlockMapper blockMapper;
    private final FollowMapper followMapper;
    private final SubscriptionMapper subscriptionMapper;

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
        member.setUniqueId(request.getUniqueId());
        member.setIntroduction(request.getIntroduction());

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileStorageService.upload(profileImage, "uploads/profiles/");
            member.setProfileImage(imageUrl);
        }

        memberMapper.updateProfile(member);
    }

    public MemberResponseDto getMemberProfile(
            Long loginMemberId,
            Long targetMemberId
    ) {
        if (blockMapper.existsBlockBetween(loginMemberId, targetMemberId)) {
            throw new IllegalArgumentException("차단 관계의 사용자입니다.");
        }

        MemberResponseDto profile =
                memberMapper.findProfileById(targetMemberId);

        if (profile == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        if (loginMemberId != null) {
            profile.setSubscribedStatus(subscriptionMapper.findByMemberIdAndCreatorId(loginMemberId, targetMemberId));
        }

        return profile;
    }

    @Transactional
    public void blockMember(Long memberId, Long blockedId) {

        if (memberId.equals(blockedId)) {
            throw new IllegalArgumentException("자기 자신은 차단할 수 없습니다.");
        }

        if (blockMapper.existsBlock(memberId, blockedId) > 0) {
            throw new IllegalArgumentException("이미 차단한 회원입니다.");
        }

        // 차단 등록
        blockMapper.saveBlock(memberId, blockedId);

        // 서로의 팔로우 관계 제거
        followMapper.deleteFollowBetween(memberId, blockedId);
    }

    public void unblockMember(Long memberId, Long blockedId) {

        blockMapper.deleteBlock(memberId, blockedId);
    }

    public List<BlockedMemberResponseDto> getBlockedMembers(Long memberId) {
        return blockMapper.findBlockedMembers(memberId);
    }

    @Transactional(readOnly = true)
    public List<RecommendedMemberResponseDto> getRecommendedMembers(Long memberId) {
        return memberMapper.findRecommendedMembers(memberId);
    }
}