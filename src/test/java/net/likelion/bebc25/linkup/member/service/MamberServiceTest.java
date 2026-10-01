package net.likelion.bebc25.linkup.member.service;

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
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("추천 사용자 조회 테스트")
    void getRecommendedMembersTest() {

        Long memberId = 1L;

        List<RecommendedMemberResponseDto> result =
                memberService.getRecommendedMembers(memberId);

        assertThat(result).isNotNull();
        assertThat(result).hasSizeLessThanOrEqualTo(4);
    }
}