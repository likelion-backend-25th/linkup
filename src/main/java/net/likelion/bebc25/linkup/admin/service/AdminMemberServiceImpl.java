package net.likelion.bebc25.linkup.admin.service;

import net.likelion.bebc25.linkup.admin.dto.AdminMemberDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberSearchRequest;
import net.likelion.bebc25.linkup.admin.mapper.AdminMemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminMemberServiceImpl implements AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;

    public AdminMemberServiceImpl(AdminMemberMapper adminMemberMapper){
        this.adminMemberMapper = adminMemberMapper;
    }

    @Override
    public List<AdminMemberResponse> findMembers(AdminMemberSearchRequest condition) {
        return adminMemberMapper.findMembers(condition);
    }

    @Override
    public long countMembers(AdminMemberSearchRequest condition) {
        return adminMemberMapper.countMembers(condition);
    }

    @Override
    public AdminMemberDetailResponse findMemberDetail(Long memberId) {
        return adminMemberMapper.findMemberDetail(memberId);
    }
}
