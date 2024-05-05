package com.batch.kata.job;

import com.batch.kata.domain.EditProduct;
import com.batch.kata.domain.Product;
import com.batch.kata.repository.EditProductRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FirstJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final EditProductRepository editProductRepository;

    private static int CHUNK_SIZE = 1;

    @Bean(name = "firstJob")
    public Job firstJob() {
        return new JobBuilder("firstJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .build();

    }

    @Bean(name = "firstStep")
    public Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .<Product, EditProduct>chunk(CHUNK_SIZE, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean(name = "firstReader")
    public ItemReader<Product> itemReader() {
        return new JpaPagingItemReaderBuilder<Product>()
                .name("jpaReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("select p from Product p")
                .build();
    }

    @Bean(name = "firstProcessor")
    public ItemProcessor<Product, EditProduct> itemProcessor() {
        return new ItemProcessor<Product, EditProduct>() {
            @Override
            public EditProduct process(Product item) throws Exception {
                System.out.println("Process : " + item.getId());
                return EditProduct.builder()
                        .type("fin")
                        .price(item.getPrice())
                        .name(item.getName())
                        .build();
            }
        };
    }

    @Bean(name = "firstWriter")
    public ItemWriter<EditProduct> itemWriter() {
        return items -> {
            items.forEach(editProductRepository::save);
        };
    }
}
