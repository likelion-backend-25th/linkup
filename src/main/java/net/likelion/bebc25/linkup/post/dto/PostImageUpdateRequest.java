package net.likelion.bebc25.linkup.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostImageUpdateRequest (
        @Schema(description = "유지할 기존 이미지 ID. 새 이미지인 경우 null")
        Long imageId,

        @Schema(description = "newImages의 인덱스. 기존 이미지인 경우 null")
        Integer newImageIndex
){
}
