package net.likelion.bebc25.linkup.security;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.member.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 공격 방어 기능 비활성화
                .csrf(csrf -> csrf.disable())

                // HTTP 기본 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // 기본 폼 로그인 비활성화
                .formLogin(form -> form.disable())

                // URL 엔드포인트별 기본 접근 인가 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**", "/oauth2/**").permitAll()
                        .anyRequest().permitAll()
                )

                // OAuth2 로그인
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/", true)
                );

        return http.build();
    }
}