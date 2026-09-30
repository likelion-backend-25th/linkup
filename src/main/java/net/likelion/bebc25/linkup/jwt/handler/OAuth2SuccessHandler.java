package net.likelion.bebc25.linkup.jwt.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.linkup.jwt.service.TokenService;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Member member = userDetails.getMember();

        Map<String, String> tokens =
                tokenService.createAccessToken(member);
        
        String redirectUrl = String.format(
                "%s/oauth/callback#accessToken=%s&refreshToken=%s",
                frontendUrl,
                tokens.get("accessToken"),
                tokens.get("refreshToken")
        );

        log.info("OAuth2 로그인 성공 - memberId: {}", member.getId());

        response.sendRedirect(redirectUrl);
    }
}