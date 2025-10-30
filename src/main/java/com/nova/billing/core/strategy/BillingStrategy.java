package com.nova.billing.core.strategy;

import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

public interface BillingStrategy {

    boolean supports(CalculationParameter param);

    Bill calculate(CalculationParameter param);
    
}
