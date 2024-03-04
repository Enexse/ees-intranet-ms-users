package com.enexse.intranet.ms.users.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job passwordExpirationJob;

    @Scheduled(cron = "0 0 0 * * ?") //Run once every day at midnight
    //@Scheduled(cron = "0 */2 * * * *") // Run once every two minutes
    public void runPasswordExpirationJob() throws Exception {
        jobLauncher.run(passwordExpirationJob, new JobParameters());
    }
}
