package net.likelion.bebc25.linkup.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenByRefreshToken {
    @Schema(example = "로그인 할때 발급 받은 리프레쉬 토큰 넣기")
    private String refreshToken;
}
