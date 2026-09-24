package net.likelion.bebc25.linkup.admin.controller;

import net.likelion.bebc25.linkup.admin.dto.AdminMemberDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberSearchRequest;
import net.likelion.bebc25.linkup.admin.service.AdminMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/members")
public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    public AdminMemberController(AdminMemberService adminMemberService){
        this.adminMemberService = adminMemberService;
    }

    // 관리자 회원 목록 조회
    @GetMapping
    public ResponseEntity<List<AdminMemberResponse>> findMembers(AdminMemberSearchRequest condition){
        return ResponseEntity.ok(adminMemberService.findMembers(condition));
    }

    // 관리자 회원 상세 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<AdminMemberDetailResponse> findMemberDetail(@PathVariable Long memberId){
        return ResponseEntity.ok(adminMemberService.findMemberDetail(memberId));
    }
}
