package net.likelion.bebc25.linkup.reply.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReplyUpdateRequest (
        @Schema(description = "댓글 본문", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "댓글 내용을 입력해 주세요.")
        @Size(max = 500)
        String content
) {
}
