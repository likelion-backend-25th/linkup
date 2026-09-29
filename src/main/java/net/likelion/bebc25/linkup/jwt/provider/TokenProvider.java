package net.likelion.bebc25.linkup.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.likelion.bebc25.linkup.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class TokenProvider {

    private final String issuer;
    private final SecretKey key;
    private final io.jsonwebtoken.JwtParser parser;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.issuer:linkup}") String issuer
    ) {
        this.issuer = issuer;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.parser = Jwts.parser()
                .verifyWith(key)
                .build();
    }

    public String generateToken(
            Member member,
            Duration expiredAt,
            boolean isAccessToken
    ) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());

        return Jwts.builder()
                .header()
                .add("type", "JWT")
                .add("alg", "HS256")
                .and()
                .claims()
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expiry)
                .subject(member.getEmail())
                .add("type", isAccessToken ? "A" : "R")
                .add("id", member.getId())
                .add("role", member.getRole())
                .and()
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String type = claims.get("type", String.class);

        if (!"A".equals(type)) {
            throw new IllegalArgumentException("Access Token이 아닙니다.");
        }

        String role = claims.get("role", String.class);

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(role)
        );

        UserDetails userDetails = User
                .withUsername(claims.getSubject())
                .password("")
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                token,
                authorities
        );
    }

    public Claims getClaims(String token) {
        Jws<Claims> jws = parser.parseSignedClaims(token);
        return jws.getPayload();
    }

    public Claims getRefreshTokenClaims(String token) {
        Claims claims = getClaims(token);

        String type = claims.get("type", String.class);

        if (!"R".equals(type)) {
            throw new IllegalArgumentException("Refresh Token이 아닙니다.");
        }

        return claims;
    }
}