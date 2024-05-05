package com.batch.kata.batch.chunk.processor;

import com.batch.kata.batch.domain.ApiRequest;
import com.batch.kata.batch.domain.ProductDto;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor2 implements ItemProcessor<ProductDto, ApiRequest> {
    @Override
    public ApiRequest process(ProductDto item) throws Exception {
        return new ApiRequest(item.id(), item);
    }
}
