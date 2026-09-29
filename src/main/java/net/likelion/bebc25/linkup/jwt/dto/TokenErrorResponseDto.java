package net.likelion.bebc25.linkup.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenErrorResponseDto {
    private String message;
}
