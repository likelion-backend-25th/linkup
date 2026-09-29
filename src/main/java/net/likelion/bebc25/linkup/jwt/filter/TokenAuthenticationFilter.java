package net.likelion.bebc25.linkup.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.jwt.provider.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String HEADER = "Authorization";
        String authorizationHeader = request.getHeader(HEADER);
        String token = getAccessToken(authorizationHeader);
        try{
            if(token != null) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            throw new JwtException("Invalid token. 유효하지 않은 토큰");
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired Token. 토큰기한 만료");
        } catch (SignatureException e) {
            throw new JwtException("Signature Failed. 인증 실패");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/actuator/health");
    }

    private String getAccessToken(String authorizationHeader) {
        String PREFIX = "Bearer ";
        if(authorizationHeader != null && authorizationHeader.startsWith(PREFIX))
            return authorizationHeader.substring(PREFIX.length());
        else
            return null;
    }
}