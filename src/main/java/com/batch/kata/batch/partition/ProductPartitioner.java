package com.batch.kata.batch.partition;

import com.batch.kata.batch.domain.ProductDto;
import com.batch.kata.batch.job.api.QueryGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ProductPartitioner implements Partitioner {
    private final DataSource dataSource;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        ProductDto[] productDtos = QueryGenerator.getProductDtos(dataSource);

        HashMap<String, ExecutionContext> result = new HashMap<>();

        int number = 0;

        for (int i = 0; i < productDtos.length; i++) {
            ExecutionContext value = new ExecutionContext();

            result.put("partition" + number, value);
            value.put("product", productDtos[i]);

            number++;
        }
        return result;
    }
}
