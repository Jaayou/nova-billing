package com.nova.billing.core.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

@Component
public class WirelessBillingStrategy implements BillingStrategy {

    private static final String WIRELESS_SERVICE_CODE = "WL";

    @Override
    public boolean supports(CalculationParameter param) {
        return param.getContractId() != null && param.getContractId().contains(WIRELESS_SERVICE_CODE);
    }

    @Override
    public Bill calculate(CalculationParameter param) {

        System.out.println("[Strategy] 무선 요금계산 전략 실행: " + param.getContractId());

        BigDecimal wirelessAmount = new BigDecimal("80000");

        return Bill.builder()
                .contractId(param.getContractId())
                .totalAmount(wirelessAmount)
                .message("무선 요금계산 완료(전략)")
                .build();

    }
}
