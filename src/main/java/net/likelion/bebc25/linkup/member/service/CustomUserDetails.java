package net.likelion.bebc25.linkup.member.service;

import net.likelion.bebc25.linkup.member.domain.Member;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes; // 소셜 프로필 속성 보관 필드 추가

    // 일반 폼 로그인 및 JWT 필터용 생성자
    public CustomUserDetails(Member member) {
        this.member = member;
        this.attributes = Collections.emptyMap();
    }

    // OAuth 2.0 소셜 로그인용 신규 생성자 추가
    public CustomUserDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    public Member getMember() {
        return member;
    }

    public Long getId() {
        return this.member.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 문자열을 SimpleGrantedAuthority 객체로 변환 (ROLE_ 접두사 필수)
        return List.of(new SimpleGrantedAuthority(member.getRole()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (true: 만료 안 됨)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부 (true: 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부 (true: 만료 안 됨)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (true: 활성화)
    }

    // 2. OAuth2User 인터페이스 구현 메서드 추가
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getEmail();
    }
}
