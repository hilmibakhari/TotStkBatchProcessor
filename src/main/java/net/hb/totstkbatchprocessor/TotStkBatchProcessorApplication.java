package net.hb.totstkbatchprocessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TotStkBatchProcessorApplication implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final Job job;

    public TotStkBatchProcessorApplication(Job job, JobLauncher jobLauncher) {
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    public static void main(String[] args) {
        SpringApplication.run(TotStkBatchProcessorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(job,
                new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
                        .toJobParameters());
    }
}
