package net.likelion.bebc25.linkup.jwt.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.linkup.jwt.service.TokenService;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String responseBody = String.format(
                """
                {
                    "message": "로그인에 성공했습니다.",
                    "accessToken": "%s",
                    "refreshToken": "%s"
                }
                """,
                tokens.get("accessToken"),
                tokens.get("refreshToken")
        );

        response.getWriter().write(responseBody);

        log.info("OAuth2 로그인 성공 - memberId: {}", member.getId());
    }
}