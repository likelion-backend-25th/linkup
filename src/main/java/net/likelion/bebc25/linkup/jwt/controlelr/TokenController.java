package net.likelion.bebc25.linkup.jwt.controlelr;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.linkup.jwt.dto.AccessTokenResponseDto;
import net.likelion.bebc25.linkup.jwt.dto.CreateAccessTokenByRefreshToken;
import net.likelion.bebc25.linkup.jwt.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDto> refreshAccessToken(
            @RequestBody CreateAccessTokenByRefreshToken request
    ) {
        return ResponseEntity.ok(
                tokenService.refreshAccessToken(request.getRefreshToken())
        );
    }
}