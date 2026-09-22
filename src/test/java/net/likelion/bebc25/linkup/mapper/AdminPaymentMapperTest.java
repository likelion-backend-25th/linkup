package net.likelion.bebc25.linkup.mapper;

import net.likelion.bebc25.linkup.admin.dto.AdminPaymentDetailResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentResponse;
import net.likelion.bebc25.linkup.admin.dto.AdminPaymentSearchRequest;
import net.likelion.bebc25.linkup.admin.mapper.AdminPaymentMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class AdminPaymentMapperTest {

    @Autowired
    private AdminPaymentMapper adminPaymentMapper;

    @Test
    @DisplayName("관리자 결제 목록 조회 테스트")
    void findPaymentTest(){

        //given
        AdminPaymentSearchRequest request = new AdminPaymentSearchRequest(
                null, null, null, null, 1, 10);

        // when
        List<AdminPaymentResponse> payments = adminPaymentMapper.findPayments(request);

        // then
        assertThat(payments).isNotNull();
    }

    @Test
    @DisplayName("관리자 결제 전체 개수 조회 테스트")
    void countPaymentTest(){

        // given
        AdminPaymentSearchRequest request = new AdminPaymentSearchRequest(
                null, null, null, null, 1, 10);

        // when
        long count = adminPaymentMapper.countPayments(request);

        // then
        // isGreaterThanOrEqualTo : >= (크거나 같은지) 를 비교하는 연산자
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("관리자 결제 상세 조회 테스트")
    void findPaymentDetailTest(){

        // given
        Long paymentId = 1L;

        // when
        AdminPaymentDetailResponse payments = adminPaymentMapper.findPaymentDetail(paymentId);

        // then
        assertThat(payments).isNotNull();
        assertThat(payments.paymentId()).isEqualTo(paymentId);
    }
}
