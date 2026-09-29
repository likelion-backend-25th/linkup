package net.likelion.bebc25.linkup.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccessTokenResponseDto {
    private String message;
    private String token;
    private String refreshToken;

    public AccessTokenResponseDto(String string) {
        this.message = string;
        this.token = null;
        this.refreshToken = null;
    }
}
