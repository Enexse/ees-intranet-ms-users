package com.enexse.intranet.ms.users.configurations;
import com.enexse.intranet.ms.users.utils.CheckPasswordExpirationTasklet;
import com.mongodb.client.MongoClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
@EnableScheduling
public class PasswordExpirationBatchJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step checkPasswordExpirationStep() {
        return stepBuilderFactory.get("checkPasswordExpirationStep")
                .tasklet(checkPasswordExpirationTasklet())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job passwordExpirationJob() {
        return jobBuilderFactory.get("passwordExpirationJob")
                .start(checkPasswordExpirationStep())
                .build();
    }

    @Bean
    public Tasklet checkPasswordExpirationTasklet() {
        return new CheckPasswordExpirationTasklet();
    }
}