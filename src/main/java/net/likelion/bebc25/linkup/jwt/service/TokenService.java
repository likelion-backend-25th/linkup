package net.likelion.bebc25.linkup.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.jwt.dto.AccessTokenResponseDto;
import net.likelion.bebc25.linkup.jwt.provider.TokenProvider;
import net.likelion.bebc25.linkup.member.domain.Member;
import net.likelion.bebc25.linkup.member.mapper.MemberMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final MemberMapper memberMapper;

    /**
     * OAuth2 로그인 성공 후 Access Token + Refresh Token 발급
     */
    public Map<String, String> createAccessToken(Member member) {

        Duration accessTokenDuration = Duration.ofHours(1);
        Duration refreshTokenDuration = Duration.ofDays(7);

        String accessToken = tokenProvider.generateToken(
                member,
                accessTokenDuration,
                true
        );

        String refreshToken = tokenProvider.generateToken(
                member,
                refreshTokenDuration,
                false
        );

        Map<String, String> response = new HashMap<>();
        response.put("message", "토큰이 발급되었습니다.");
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        return response;
    }

    /**
     * Refresh Token으로 새로운 Access Token 발급
     */
    public AccessTokenResponseDto refreshAccessToken(String refreshToken) {

        Claims claims;

        try {
            claims = tokenProvider.getRefreshTokenClaims(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException(
                    "Refresh Token이 만료되었습니다."
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "유효하지 않은 Refresh Token입니다."
            );
        }

        Long memberId = claims.get("id", Long.class);
        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        String accessToken = tokenProvider.generateToken(
                member,
                Duration.ofHours(1),
                true
        );

        String newRefreshToken = tokenProvider.generateToken(
                member,
                Duration.ofDays(7),
                false
        );


        return new AccessTokenResponseDto(
                "Access Token이 재발급되었습니다.",
                accessToken,
                newRefreshToken
        );
    }
}