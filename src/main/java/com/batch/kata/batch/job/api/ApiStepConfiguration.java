package com.batch.kata.batch.job.api;

import com.batch.kata.batch.chunk.processor.ApiItemProcessor1;
import com.batch.kata.batch.chunk.processor.ApiItemProcessor2;
import com.batch.kata.batch.chunk.processor.ApiItemProcessor3;
import com.batch.kata.batch.chunk.writer.ApiItemWriter1;
import com.batch.kata.batch.chunk.writer.ApiItemWriter2;
import com.batch.kata.batch.chunk.writer.ApiItemWriter3;
import com.batch.kata.batch.classifier.ProcessorClassifier;
import com.batch.kata.batch.classifier.WriterClassifier;
import com.batch.kata.batch.domain.ApiRequest;
import com.batch.kata.batch.domain.Product;
import com.batch.kata.batch.domain.ProductDto;
import com.batch.kata.batch.partition.ProductPartitioner;
import com.batch.kata.service.ApiService1;
import com.batch.kata.service.ApiService2;
import com.batch.kata.service.ApiService3;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final ApiService1 apiService1;
    private final ApiService2 apiService2;
    private final ApiService3 apiService3;

    private static int CHUNK_SIZE = 10;

    @Bean
    public Step apiMasterStep() throws Exception {
        return new StepBuilder("apiMasterStep", jobRepository)
                .partitioner(apiSlaveStep().getName(), new ProductPartitioner(dataSource))
                .step(apiSlaveStep())
                .gridSize(3)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step apiSlaveStep() throws Exception {
        return new StepBuilder("apiSlaveStep", jobRepository)
                .<ProductDto, Product>chunk(CHUNK_SIZE, transactionManager)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();

    }

    @Bean
    public ItemWriter itemWriter() {
        ClassifierCompositeItemWriter<ApiRequest> writer
                = new ClassifierCompositeItemWriter<>();
        Map<String, ItemWriter<ApiRequest>> writerMap = new HashMap<>();
        writerMap.put("1", new ApiItemWriter1(apiService1));
        writerMap.put("2", new ApiItemWriter2(apiService2));
        writerMap.put("3", new ApiItemWriter3(apiService3));

        WriterClassifier<ApiRequest, ItemWriter<? super ApiRequest>> classifier = new WriterClassifier<>(writerMap);

        writer.setClassifier(classifier);
        return writer;
    }

    @Bean
    public ItemProcessor itemProcessor() {
        ClassifierCompositeItemProcessor<ProductDto, ApiRequest> processor
                = new ClassifierCompositeItemProcessor<>();
        Map<String, ItemProcessor<ProductDto, ApiRequest>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());

        ProcessorClassifier<ProductDto, ItemProcessor<?, ? extends ApiRequest>> classifier = new ProcessorClassifier(processorMap);

        processor.setClassifier(classifier);
        return processor;
    }

    @Bean
    public ItemReader<ProductDto> itemReader(@Value("#{stepExecutionContext[product]}") ProductDto productDto) throws Exception {
        JdbcPagingItemReader<ProductDto> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(CHUNK_SIZE);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductDto.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKey = new HashMap<>();
        sortKey.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKey);

        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }
}
