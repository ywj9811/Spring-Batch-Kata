package com.batch.kata.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import java.time.Duration;

@Slf4j
public class JobListener {
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job 시작");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        long time = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime())
                .toMillis();

        log.info("Job 소요 시간 : {}", time);
    }
}
