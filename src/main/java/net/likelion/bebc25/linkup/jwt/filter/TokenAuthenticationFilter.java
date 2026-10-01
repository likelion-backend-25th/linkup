package net.likelion.bebc25.linkup.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.linkup.jwt.provider.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        String token = getAccessToken(authorizationHeader);

        // JWT 디버깅
        log.info("========== JWT DEBUG ==========");
        log.info("요청 URL: {}", request.getRequestURI());
        log.info("Authorization Header: {}", authorizationHeader);
        log.info("JWT Token: {}", token);
        log.info("==============================");

        try {
            if (token != null) {

                Authentication authentication =
                        tokenProvider.getAuthentication(token);

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                log.info("JWT 인증 성공 - user: {}",
                        authentication.getName());
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.error("JWT 만료", e);
            throw new JwtException("Expired Token. 토큰기한 만료");

        } catch (SignatureException e) {
            log.error("JWT 서명 검증 실패", e);
            throw new JwtException("Signature Failed. 인증 실패");

        } catch (IllegalArgumentException e) {
            log.error("JWT 잘못된 토큰", e);
            throw new JwtException("Invalid token. 유효하지 않은 토큰");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/actuator/health");
    }

    private String getAccessToken(String authorizationHeader) {

        String PREFIX = "Bearer ";

        if (authorizationHeader != null
                && authorizationHeader.startsWith(PREFIX)) {

            return authorizationHeader.substring(PREFIX.length());
        }

        return null;
    }
}