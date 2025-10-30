package com.nova.billing.core;

import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

public interface CalculationService {
    Bill calculate(CalculationParameter param);
}
