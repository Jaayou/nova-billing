package com.nova.billing.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nova.billing.core.CalculationService;
import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HotbillController {

    private final CalculationService calculationService;

    @PostMapping("/hotbill")
    public Bill calculateHotbill(@RequestBody HotbillRequest request) {

        System.out.println("[HotbillController] 핫빌 요청 수신: " + request.getContractId());

        CalculationParameter param = CalculationParameter.builder()
                .contractId(request.getContractId())
                .isHotbill(true)
                .build();

        Bill billResult = calculationService.calculate(param);

        return billResult;
    }
}
