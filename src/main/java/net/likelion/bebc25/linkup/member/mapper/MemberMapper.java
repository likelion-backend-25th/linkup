package net.likelion.bebc25.linkup.member.mapper;

import net.likelion.bebc25.linkup.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    // 이메일 기반 회원 정보 조회
    Member findByEmail(@Param("email") String email);

    // 회원 id 기반 정보 조회
    Member findById(@Param("id") Long id);

    // 신규 회원 등록 (소셜 로그인 자동 회원가입)
    int save(Member member);
}
