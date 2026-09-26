package net.likelion.bebc25.linkup.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @Schema(
                description = "게시글 본문 내용",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "본문 내용은 필수입니다.")
        @Size(max = 2000, message = "본문은 2000자 이하여야 합니다.")
        String content,

        @Schema(description = "구독자 전용 게시글 여부")
        @NotNull
        boolean subscriberOnly,

        @Schema(description = "파일 삭제 여부")
        boolean removeFile
) {
}
