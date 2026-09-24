package net.likelion.bebc25.linkup.report.controller;

import jakarta.validation.Valid;
import net.likelion.bebc25.linkup.member.service.CustomUserDetails;
import net.likelion.bebc25.linkup.report.dto.ReportCreateRequest;
import net.likelion.bebc25.linkup.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    // 게시글 신고
    @PostMapping("/{postId}/reports")
    public ResponseEntity<Void> createPostReport(
            @PathVariable Long postId,
            @Valid @RequestBody ReportCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

            Long memberId = userDetails.getId();

            reportService.createdPostReport(memberId, postId, request);

            return ResponseEntity.ok().build();
    }
}

