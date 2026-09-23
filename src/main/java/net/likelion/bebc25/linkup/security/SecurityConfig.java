package net.likelion.bebc25.linkup.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // CSRF 공격 방어 기능 비활성화
                .csrf(csrf -> csrf.disable())
                // HTTP 기본 인증 활성화
                .httpBasic(basic -> basic.disable())
                // 기본 폼 로그인 비활성화
                .formLogin(form -> form.disable())
                // URL 엔드포인트별 기본 접근 인가 설정
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll());

        return http.build();
    }
}
