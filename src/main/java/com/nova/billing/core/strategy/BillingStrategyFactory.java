package com.nova.billing.core.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nova.billing.domain.CalculationParameter;

@Component
public class BillingStrategyFactory {

    private final List<BillingStrategy> strategies;

    public BillingStrategyFactory(List<BillingStrategy> strategies) {
        this.strategies = strategies;

        System.out.println("===== 로드된 요금계산 전략 수: " + strategies.size() + "개 =====");
        strategies.forEach(s -> System.out.println("  - " + s.getClass().getSimpleName()));
    }

    public BillingStrategy findStrategy(CalculationParameter param) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(param))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "지원하는 요금계산 전략을 찾을 수 없음: " + param.getContractId()));
    }
}
