package net.hb.totstkbatchprocessor.config;

import net.hb.totstkbatchprocessor.exception.InvalidHeaderException;
import net.hb.totstkbatchprocessor.processor.TotStkProcessor;
import net.hb.totstkbatchprocessor.record.InputLineMapper;
import net.hb.totstkbatchprocessor.writer.OutputRecordFieldExtractor;
import net.hb.totstkbatchprocessor.record.OutputRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;

/**
 * created: 20240927
 * author : hilmi
 */
@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("fileProcessingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Object, OutputRecord>chunk(10, transactionManager)
                .reader(multiResourceItemReader())
                .processor(new TotStkProcessor())
                .writer(writer())
                .faultTolerant()
                .skip(InvalidHeaderException.class)
                .skipLimit(100)
                .transactionManager(transactionManager)
                .build();
    }


    @Bean
    public MultiResourceItemReader<Object> multiResourceItemReader() {
        // Specify the directory containing the input files
        Resource[] resources = getInputFiles();

        return new MultiResourceItemReaderBuilder<Object>()
                .name("multiFileReader")
                .resources(resources)
                .delegate(fileReader())  // Use FlatFileItemReader for each file
                .build();
    }


    // Method to gather input files from the "input" directory
    private Resource[] getInputFiles() {
        File dir = new File("input");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt")); // Filter to read only .txt files

        if (files != null) {
            Resource[] resources = new Resource[files.length];
            for (int i = 0; i < files.length; i++) {
                resources[i] = new FileSystemResource(files[i]);
            }
            return resources;
        } else {
            throw new IllegalStateException("No input files found in directory: " + dir.getAbsolutePath());
        }
    }

    @Bean
    public FlatFileItemReader<Object> fileReader() {
        return new FlatFileItemReaderBuilder<Object>()
                .name("fileReader")
                .lineMapper(new InputLineMapper())  // Use the custom LineMapper defined earlier
                .build();
    }

    @Bean
    public FlatFileItemWriter<OutputRecord> writer() {
        DelimitedLineAggregator<OutputRecord> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");  // Tab delimited
        lineAggregator.setFieldExtractor(new OutputRecordFieldExtractor());

        return new FlatFileItemWriterBuilder<OutputRecord>()
                .name("outputFileWriter")
                .resource(new FileSystemResource("output/output.txt"))  // Output file path
                .lineAggregator(lineAggregator)
                .build();
    }

}
