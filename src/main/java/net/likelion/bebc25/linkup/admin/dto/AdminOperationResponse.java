package net.likelion.bebc25.linkup.admin.dto;

public record AdminOperationResponse(
        int pendingReportCount,
        int totalMember
) {
}
