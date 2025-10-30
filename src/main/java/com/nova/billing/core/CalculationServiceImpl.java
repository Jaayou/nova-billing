package com.nova.billing.core;

import org.springframework.stereotype.Service;

import com.nova.billing.core.strategy.BillingStrategy;
import com.nova.billing.core.strategy.BillingStrategyFactory;
import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final BillingStrategyFactory strategyFactory;
    
    @Override
    public Bill calculate(CalculationParameter param) {

        System.out.println("[CalculationService] 계약ID]" + param.getContractId());
        System.out.println("[CalculationService] 핫빌여부]" + param.isHotbill());

        BillingStrategy strategy = strategyFactory.findStrategy(param);

        Bill result = strategy.calculate(param);

        if (param.isHotbill()) {
            System.out.println("[CalculationService] 핫빌 요청으로 처리됨.");        
        }

        System.out.println("[CalculationService] 계산완료. 최종 금액: " + result.getTotalAmount());

        return result;

    }
}
