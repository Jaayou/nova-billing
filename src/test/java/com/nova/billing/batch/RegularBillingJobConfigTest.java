package com.nova.billing.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBatchTest
@SpringBootTest
class RegularBillingJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private ApplicationContext applicationContext;

    @AfterEach
    public void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    @DisplayName("정기청구배치 Job(regularBillingJob) 전체 흐름 테스트 (Reader->Processor->Writer)")
    void testRegularBillingJob() throws Exception {

        // 1. Arrange
        Job regularBillingJob = (Job)applicationContext.getBean("regularBillingJob");
        jobLauncherTestUtils.setJob(regularBillingJob);

        JobParameters jobParameters = new JobParameters();

        // 2. Act
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // 3. Assert
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus(), "Job이 성공적으로 완료되어야 한다.");
        assertEquals(1, jobExecution.getStepExecutions().size(), "Step은 1개여야 한다.");

        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertEquals(5, stepExecution.getReadCount(), "Reader가 5건을 읽어야 한다.");
        assertEquals(5, stepExecution.getProcessCount(), "Processor가 5건을 처리해야 한다.");
        assertEquals(5, stepExecution.getWriteCount(), "Writer가 5건을 써야 한다.");
    }
}
