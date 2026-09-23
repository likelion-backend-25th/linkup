package net.likelion.bebc25.linkup.member.service;


import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.mapper.MemberMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberMapper.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("해당 이메일을 사용하는 사용자를 찾을수 없습니다: " + email);
        }
        return new CustomUserDetails(member);
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Member member = memberMapper.findById(id);
        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return new CustomUserDetails(member);
    }
}