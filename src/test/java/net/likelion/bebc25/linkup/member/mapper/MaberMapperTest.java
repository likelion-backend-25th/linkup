package net.likelion.bebc25.linkup.member.mapper;

import net.likelion.bebc25.linkup.member.dto.RecommendedMemberResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("추천 사용자 조회 - 본인, 팔로우, 차단 사용자를 제외하고 팔로워 수가 많은 4명 조회")
    void findRecommendedMembersTest() {

        // given
        Long memberId = 1L;

        // when
        List<RecommendedMemberResponseDto> result =
                memberMapper.findRecommendedMembers(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSizeLessThanOrEqualTo(4);

        // 본인 제외
        assertThat(result)
                .noneMatch(member -> member.id().equals(memberId));

        // 팔로워 수 내림차순 확인
        assertThat(result)
                .extracting(RecommendedMemberResponseDto::followerCount)
                .isSortedAccordingTo((a, b) -> Integer.compare(b, a));
    }


}