package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminMemberDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberSearchRequest;
import net.likelion.bebc25.linkup.admin.mapper.AdminMemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminMemberTest {

    @Autowired
    private AdminMemberMapper adminMemberMapper;

    @Test
    @DisplayName("관리자 회원 목록 조회")
    void findMembersTest(){
        // given
        AdminMemberSearchRequest condition = new AdminMemberSearchRequest(
                null, null, null, null, 1, 10);
        //when
        List<AdminMemberResponse> members = adminMemberMapper.findMembers(condition);
        //then
        assertThat(members).isNotNull();
    }

    @Test
    @DisplayName("관리자 회원 닉네임 검색")
    void findMembersByNicknameTest(){
        //given
        AdminMemberSearchRequest condition = new AdminMemberSearchRequest(
                "test", "nickname", null, null, 1, 10);
        // when
        List<AdminMemberResponse> members = adminMemberMapper.findMembers(condition);
        // then
        assertThat(members).isNotNull();

        for(AdminMemberResponse member : members){
            assertThat(member.nickname().contains("test")).isTrue();
        }
    }

    @Test
    @DisplayName("관리자 회원 아이디 검색")
    void findMembersByUserIdTest(){
        //given
        AdminMemberSearchRequest condition = new AdminMemberSearchRequest(
                "test", "userId", null, null, 1, 10);
        // when
        List<AdminMemberResponse> members = adminMemberMapper.findMembers(condition);
        // then
        assertThat(members).isNotNull();

        for(AdminMemberResponse member : members){
            assertThat(member.nickname().contains("test"));
        }
    }

    @Test
    @DisplayName("관리자 회원 상세 조회")
    void findMemberDetailTest() {
        Long memberId = 1L;

        AdminMemberDetailResponse member = adminMemberMapper.findMemberDetail(memberId);

        assertThat(member).isNotNull();
        assertThat(member.id()).isEqualTo(memberId);
        assertThat(member.nickname()).isNotNull();
        assertThat(member.userId()).isNotNull();
        assertThat(member.email()).isNotNull();
        assertThat(member.memberStatus()).isNotNull();
        assertThat(member.creatorStatus()).isNotNull();
        assertThat(member.createdAt()).isNotNull();
    }


    @Test
    @DisplayName("관리자 회원 수 조회")
    void countMembersTest() {
        // given
        AdminMemberSearchRequest condition = new AdminMemberSearchRequest(
                null, null, null, null, 1, 10
        );

        // when
        long count = adminMemberMapper.countMembers(condition);

        // then
        assertThat(count).isGreaterThanOrEqualTo(0);
    }


    @Test
    @DisplayName("관리자 회원 상태 필터")
    void findMembersByMemberStatusTest() {
        // given
        AdminMemberSearchRequest condition = new AdminMemberSearchRequest(
                null, null, "ACTIVE", null, 1, 10
        );

        // when
        List<AdminMemberResponse> members =
                adminMemberMapper.findMembers(condition);

        // then
        assertThat(members).isNotNull();

        for (AdminMemberResponse member : members) {
            assertThat(member.memberStatus()).isEqualTo("ACTIVE");
        }
    }

    @Test
    @DisplayName("관리자 회원 상태 필터")
    void findMembersByCreatorStatusTest() {
        // given
        AdminMemberSearchRequest condition = new AdminMemberSearchRequest(
                null, null, "Creator", null, 1, 10
        );

        // when
        List<AdminMemberResponse> members =
                adminMemberMapper.findMembers(condition);

        // then
        assertThat(members).isNotNull();

        for (AdminMemberResponse member : members) {
            assertThat(member.memberStatus()).isEqualTo("Creator");
        }
    }
}

