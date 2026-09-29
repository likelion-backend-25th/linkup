package net.likelion.bebc25.linkup.member.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.dto.MemberDto;
import net.likelion.bebc25.linkup.member.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;

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
}