package net.likelion.bebc25.linkup.admin.service;

import net.likelion.bebc25.linkup.admin.dto.AdminMemberDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberSearchRequest;

import java.util.List;

public interface AdminMemberService {
    // 관리자 회원 목록 조회
    List<AdminMemberResponse> findMembers(AdminMemberSearchRequest condition);

    long countMembers(AdminMemberSearchRequest condition);

    //관리자 회원 상세 조회
    AdminMemberDetailResponse findMemberDetail(Long memberId);
}
