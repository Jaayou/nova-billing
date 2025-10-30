package com.nova.billing.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

@SpringBootTest
public class CalculationServiceImplTest {

    @Autowired
    private CalculationService calculationService;

    @Test
    @DisplayName("[성공] '무선(WL)' 서비스 요금 계산 시 80,000원을 반환해야 한다.")
    void testCalculate_Wireless_Success() {
        // 1. Arrange
        CalculationParameter param = CalculationParameter.builder()
                .contractId("CNTRCT_WL_001")
                .isHotbill(false)
                .build();

        // 2. Act
        Bill result = calculationService.calculate(param);

        // 3. Assert
        assertNotNull(result);
        assertEquals("CNTRCT_WL_001", result.getContractId());

        // BigDecimal 타입 비교
        assertEquals(0, result.getTotalAmount().compareTo(new BigDecimal("80000")),
                "계산된 금액은 80,000원이어야 한다.");

        // 하드코딩된 메시지가 정확한 지 확인
        assertEquals("무선 요금계산 완료(전략)", result.getMessage());
    }

    @Test
    @DisplayName("[성공] '유선(WD)' 서비스(핫빌) 요금 계산 시 45,000원을 반환해야 한다.")
    void testCalculate_Wired_Success() {

        // 1. Arrange
        CalculationParameter param = CalculationParameter.builder()
                .contractId("CNTRCT_WD_002")
                .isHotbill(true)
                .build();

        // 2. Act
        Bill result = calculationService.calculate(param);

        // 3. Assert
        assertNotNull(result);
        assertEquals("CNTRCT_WD_002", result.getContractId());
        assertEquals(0, result.getTotalAmount().compareTo(new BigDecimal("45000")),
                "계산된 금액은 45,000원이어야 한다.");
        assertEquals("유선 요금계산 완료(전략)", result.getMessage());
    }

    @Test
    @DisplayName("[실패] 지원하지 않는 서비스 ID 입력 시 IllegalArgumentException이 발생해야 한다.")
    void testCalculate_UnsupportedStrategy_ThrowsException() {
        // 1. Arrange
        CalculationParameter param = CalculationParameter.builder()
                .contractId("CNTRCT_UNKNOWN_999")
                .isHotbill(false)
                .build();

        // 2. Act & 3. Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    calculationService.calculate(param);
                });

        String expetededMessage = "지원하는 요금계산 전략을 찾을 수 없음";
        assertTrue(exception.getMessage().contains(expetededMessage),
                "예외 메시지가 기대와 다름");

        System.out.println("정상적으로 예외 발생: " + exception.getMessage());                    
    }
}
