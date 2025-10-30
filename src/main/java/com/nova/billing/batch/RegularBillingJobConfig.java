package com.nova.billing.batch;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.nova.billing.core.CalculationService;
import com.nova.billing.domain.Bill;
import com.nova.billing.domain.CalculationParameter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RegularBillingJobConfig {

    private final CalculationService calculationService;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private static final int CHUNK_SIZE = 10;

    // 1. Job 정의
    @Bean
    public Job regularBillingJob() {
        return new JobBuilder("regularBillingJob", jobRepository)
                .start(regularBillingStep())
                .build();
    }

    // 2. Step 정의
    @Bean
    public Step regularBillingStep() {
        return new StepBuilder("regularBillingStep", jobRepository)
                .<CalculationParameter, Bill>chunk(CHUNK_SIZE, transactionManager)
                .reader(dummyItemReader())
                .processor(billingItemProcessor())
                .writer(logItemWriter())
                .build();
    }

    // 3. Reader 정의 (임시 데이터)
    @Bean
    public ItemReader<CalculationParameter> dummyItemReader() {
        List<CalculationParameter> dummyData = Arrays.asList(
                CalculationParameter.builder().contractId("CNTRCT_WL_001").isHotbill(false).build(),
                CalculationParameter.builder().contractId("CNTRCT_WD_002").isHotbill(false).build(),
                CalculationParameter.builder().contractId("CNTRCT_WL_003").isHotbill(false).build(),
                CalculationParameter.builder().contractId("CNTRCT_WD_004").isHotbill(false).build(),
                CalculationParameter.builder().contractId("CNTRCT_WL_005").isHotbill(false).build());

        return new ListItemReader<>(dummyData);
    }

    // 4. Processor 정의 (핵심 로직 재사용)
    @Bean
    public ItemProcessor<CalculationParameter, Bill> billingItemProcessor() {
        return param -> {
            System.out.println("[Batch Processor] 계산 시작: " + param.getContractId());

            Bill calculatedBill = calculationService.calculate(param);
            
            return calculatedBill;
        };
    }

    // 5. Writer 정의 (임시 콘솔 출력)
    @Bean
    public ItemWriter<Bill> logItemWriter() {
        return chunk -> {
            System.out.println("[Batch Writer] " + chunk.getItems().size() + "개 Bill 데이터 콘솔 출력 시작 ---");
            for (Bill bill : chunk.getItems()) {
                System.out.println("  -> [Writer] " + bill.toString());
            }
            System.out.println("[Batch Writer] --- 출력 완료 (트랜잭션 커밋)");
        };
    }
}
