package com.nova.billing.core.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

@Component
public class WiredBillingStrategy implements BillingStrategy {

    private static final String WIRED_SERVICE_CODE = "WD";

    @Override
    public boolean supports(CalculationParameter param) {
        return param.getContractId() != null && param.getContractId().contains(WIRED_SERVICE_CODE);
    }

    @Override
    public Bill calculate(CalculationParameter param) {
        System.out.println("[Strategy] 유선 요금계산 전략 실행: " + param.getContractId());

        BigDecimal wiredAmount = new BigDecimal("45000");

        return Bill.builder()
                .contractId(param.getContractId())
                .totalAmount(wiredAmount)
                .message("유선 요금계산 완료(전략)")
                .build();
    }
}
