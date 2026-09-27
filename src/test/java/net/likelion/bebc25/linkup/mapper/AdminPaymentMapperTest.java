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

import java.time.LocalDate;
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
                null, null, null, null, 1, 10
        );

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
                null, null, null, null, 1, 10
        );

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


    @Test
    @DisplayName("관리자 결제 상태 필터")
    void findPaymentByStatusTest(){
        // given
        AdminPaymentSearchRequest request = new AdminPaymentSearchRequest(
                null, "PAID", null, null, 1, 10
        );

        // when
        List<AdminPaymentResponse> payments = adminPaymentMapper.findPayments(request);

        // then
        assertThat(payments).isNotNull();
        for(AdminPaymentResponse payment : payments){
            assertThat(payment.paymentStatus()).isEqualTo("PAID");
        }
    }


    @Test
    @DisplayName("관리자 결제 구매자 닉네임 검색")
    void findPaymentsByNicknameTest(){
        // given
        AdminPaymentSearchRequest request = new AdminPaymentSearchRequest(
                "test", null, null, null, 1, 10
        );

        // whne
        List<AdminPaymentResponse> payments = adminPaymentMapper.findPayments(request);

        // then
        assertThat(payments).isNotNull();
        for(AdminPaymentResponse payment : payments){
            assertThat(payment.buyerNickname())
                    .contains("test");
        }
    }


    @Test
    @DisplayName("관리자 결제 구매자 아이디 검색")
    void findPaymentsByIdTest(){
        // given
        AdminPaymentSearchRequest request = new AdminPaymentSearchRequest(
                "test", null, null, null, 1, 10
        );

        // whne
        List<AdminPaymentResponse> payments = adminPaymentMapper.findPayments(request);

        // then
        assertThat(payments).isNotNull();
        for(AdminPaymentResponse payment : payments){
            assertThat(payment.buyerNickname())
                    .contains("test");
        }
    }

    @Test
    @DisplayName("관리자 결제 날짜 범위 검색")
    void findPaymentsDateTest(){
        // given
        LocalDate startDate = LocalDate.of(2026,9,27);
        LocalDate endDate = LocalDate.of(2026,10,5);

        AdminPaymentSearchRequest request = new AdminPaymentSearchRequest(
                null, null, startDate, endDate, 1, 10
        );

        // when
        List<AdminPaymentResponse> payments = adminPaymentMapper.findPayments(request);

        // then
        assertThat(payments).isNotNull();
        for(AdminPaymentResponse payment : payments){
            assertThat(payment.paymentDate().toLocalDate())
                    .isBetween(startDate, endDate);
        }
    }
}
