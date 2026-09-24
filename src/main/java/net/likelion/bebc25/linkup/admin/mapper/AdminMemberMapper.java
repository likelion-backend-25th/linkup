package net.likelion.bebc25.linkup.admin.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminMemberDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminMemberSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMemberMapper {

    List<AdminMemberResponse> findMembers(AdminMemberSearchRequest request);

    long countMembers(AdminMemberSearchRequest condition);

    AdminMemberDetailResponse findMemberDetail(Long memberId);
}
