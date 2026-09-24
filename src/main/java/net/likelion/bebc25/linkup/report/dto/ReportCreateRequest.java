package net.likelion.bebc25.linkup.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReportCreateRequest(
        // 신고 사유
        @NotBlank
        String reason,

        // 신고 내용
        @Size(max = 1000, message = "신고 내용은 최대 1000자까지 입력할 수 있습니다.")
        String content
) {
}
